(ns main
  (:require [babashka.cli :as cli]
            slack))

(defn -main [& args]
  (let [opts (cli/parse-opts args)]
    (println opts)
    (slack/chat (or (:text opts) "hello from bb-slack!"))))
