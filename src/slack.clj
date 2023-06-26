(ns slack
  (:require [babashka.curl :as curl]
            [cheshire.core :as json]
            [clojure.pprint :refer [pprint]]
            [clojure.string :as str]))

;; token from https://api.slack.com/authentication/oauth-v2
(def env-slack-token (System/getenv "SLACK_TOKEN"))
(def env-channel-id (System/getenv "CHANNEL_ID"))

(defn slack-api [& endpoints] (str/join "/" (cons "https://slack.com/api" endpoints)))

(defn chat [{:slack/keys [text token channel-id]}]
  (println "sending" text "to" (slack-api "chat.postMessage") text)
  (let [res (curl/post (slack-api "chat.postMessage")
                       {:headers {"Authorization" (str "Bearer " (or token env-slack-token))
                                  "Content-Type" "application/json; charset=utf-8"}
                        :body (json/generate-string {:channel (or channel-id env-channel-id)
                                                     :text text
                                                     ;; building with https://app.slack.com/block-kit-builder/
                                                     #_#_:blocks (-> (json/parse-string
                                                                      (slurp (io/resource "block.json")))
                                                                     (get "blocks"))})})]
    (pprint (update res :body json/parse-string))))
