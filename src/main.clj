(ns main
  (:require [babashka.cli :as cli]
            slack
            [util :refer [exit read-edn]]
            [clojure.edn :as edn]))

;; send to slack!
(defn -main
  "available flags
   --help
   --slack/token
   --slack/channel-id

   --slack/text

   --slack/template
   --slack/context-edn
   --slack/context-str"
  [& args]
  (let [{:as opts :keys [help]
         :slack/keys [token channel-id text template context-edn context-str]} (cli/parse-opts args)]
    (println "[bb-slack] flags:" opts)
    (cond help
          (println (:doc (meta #'-main)))

          (and (some? template) (nil? context-edn) (nil? context-str))
          (slack/template-chat! token channel-id template)

          (or (some? context-edn) (some? context-str))
          (cond (nil? template)
                (exit #:error{:message "provide template json for the template data with --slack/template"})

                (some? context-edn)
                (let [context (read-edn context-edn)]
                  (slack/template-chat! token channel-id template context))

                (some? context-str)
                (let [context (edn/read-string context-str)]
                  (slack/template-chat! token channel-id template context)))

          (some? text)
          (slack/text-chat! token channel-id text)

          :else (exit #:error{:message "please provide the proper flags"
                              :help "use --help for more information"}))
    (println "[bb-slack] done!")))
