(ns main
  (:require [babashka.cli :as cli]
            slack
            [util :refer [exit]]))

;; send to slack!
(defn -main
  "available flags
   --help
   --slack/token
   --slack/channel-id

   --slack/text

   --slack/template
   --slack/template-edn"
  [& args]
  (let [{:as opts :keys [help]
         :slack/keys [token channel-id text template context-edn]} (cli/parse-opts args)]
    (println "[bb-slack] flags:" opts)
    (cond help
          (println (:doc (meta #'-main)))

          (some? template)
          (slack/template-chat! token channel-id template context-edn)

          (and (nil? template) (some? context-edn))
          (exit #:error{:message "provide template json for the template data with --slack/template"})

          (some? text)
          (slack/text-chat! token channel-id text)

          :else (exit #:error{:message "please provide the proper flags"
                              :help "use --help for more information"}))
    (println "[bb-slack] done!")))
