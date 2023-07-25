(ns main
  (:require [babashka.cli :as cli]
            slack
            [util :refer [exit read-edn]]
            [clojure.edn :as edn]))

(defmacro syms->map [& syms] (zipmap (map keyword syms) syms))

;; send to slack!
(defn -main
  "available flags
   --help
   --slack/token
   --slack/channel-id

   --slack/text

   --slack/template
   --slack/context-edn
   --slack/context-str
   
   --slack/thread-ts
   --slack/reply-broadcast"
  [& args]
  (let [{:as _opts :keys [help]
         :slack/keys [token channel-id thread-ts reply-broadcast
                      text template context-edn context-str]} (cli/parse-opts args {:coerce {:slack/thread-ts :string}})
        opts (syms->map token channel-id thread-ts reply-broadcast)]
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
                  (slack/template-chat! opts template context))

                (some? context-str)
                (let [context (edn/read-string context-str)]
                  (slack/template-chat! opts template context)))

          (some? text)
          (slack/text-chat! opts text)

          :else (exit #:error{:message "please provide the proper flags"
                              :help "use --help for more information"}))
    (println "[bb-slack] done!")))
