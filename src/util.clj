(ns util
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]])
  (:import [java.io PushbackReader]))


(defn exit
  ([] (System/exit 1))
  ([info] (pprint info) (System/exit 1)))

(defn read-edn [file]
  (try (edn/read (PushbackReader. (io/reader file)))
       (catch Throwable e
         (println (.getMessage e))
         nil)))