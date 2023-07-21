(ns main
  (:require [babashka.cli :as cli]
            slack))

;; send to slack!
(defn -main [& args]
  (let [{:as opts
         :keys [help]
         :slack/keys [text token channel-id]} (cli/parse-opts args)]
    (println opts)
    (if help
      (println "hello from bb-slack")
      (slack/chat #:slack{:token token
                          :channel-id channel-id
                          :text (or text "hello from bb-slack!")}))))
