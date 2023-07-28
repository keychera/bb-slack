(ns slack
  (:require [babashka.curl :as curl]
            [cheshire.core :as json]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [selmer.parser :refer [render]]))

;; token from https://api.slack.com/authentication/oauth-v2
(def env-slack-token (System/getenv "SLACK_TOKEN"))
(def env-channel-id (System/getenv "CHANNEL_ID"))

(defn slack-api [& endpoints] (str/join "/" (cons "https://slack.com/api" endpoints)))

(defn chat-postMessage! [{:keys [token channel-id thread-ts reply-broadcast] :as _opts} body]
  (println "to" (slack-api "chat.postMessage"))
  (let [req {:headers {"Authorization" (str "Bearer " (or token env-slack-token))
                       "Content-Type" "application/json; charset=utf-8"}
             :body (json/generate-string
                    (merge {:channel (or channel-id env-channel-id)
                            :thread_ts thread-ts
                            :reply_broadcast (or reply-broadcast false)}
                           body))}
        res (curl/post (slack-api "chat.postMessage") req)]
    (pprint (update res :body json/parse-string))))

(defn text-chat! [opts text]
  (println "sending text: " text)
  (chat-postMessage! opts {:text text}))

(defn template-chat!
  ([opts template]
   (template-chat! opts template {}))
  ([opts template context]
   (let [template-str (slurp template)
         chat-to-send (render template-str context)]
     (println "sending template from" template)
     (println "with context data: " context)
     (chat-postMessage! opts {:blocks (-> (json/parse-string chat-to-send) (get "blocks"))}))))
