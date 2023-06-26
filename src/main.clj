(ns main
  (:require [babashka.cli :as cli]
            slack))

(defn -main [& args]
  (let [{:as opts
         :slack/keys [text token channel-id]} (cli/parse-opts args)]
    (println opts)
    (slack/chat #:slack{:token token
                        :channel-id channel-id
                        :text (or text "hello from bb-slack!")})))
