(ns slack
  (:require [babashka.curl :as curl]
            [cheshire.core :as json]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]
            [selmer.parser :refer [render]]
            [util :refer [read-edn]]))

;; token from https://api.slack.com/authentication/oauth-v2
(def env-slack-token (System/getenv "SLACK_TOKEN"))
(def env-channel-id (System/getenv "CHANNEL_ID"))

(defn slack-api [& endpoints] (str/join "/" (cons "https://slack.com/api" endpoints)))

(defn chat-postMessage! [token channel-id body]
  (println "to" (slack-api "chat.postMessage"))
  (let [req {:headers {"Authorization" (str "Bearer " (or token env-slack-token))
                       "Content-Type" "application/json; charset=utf-8"}
             :body (json/generate-string
                    (merge {:channel (or channel-id env-channel-id)} body))}
        res (curl/post (slack-api "chat.postMessage") req)]
    (println req)
    (pprint (update res :body json/parse-string))))

(defn text-chat! [token channel-id text]
  (println "sending text: " text)
  (chat-postMessage! token channel-id {:text text}))

(defn template-chat! [token channel-id template context-edn]
  (let [template-str (slurp template)
        context (read-edn context-edn)
        chat-to-send (render template-str context)]
    (println "sending template from" template)
    (if context-edn
      (println "with context data: " context)
      (println "without context data"))
    (chat-postMessage! token channel-id {:blocks (-> (json/parse-string chat-to-send)
                                                     (get "blocks"))})))
