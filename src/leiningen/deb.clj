(ns leiningen.deb
  (use leiningen.tasks)
  (require [clojure.contrib.string :as s]))

(defn deb
  [project]
  (let [[upstream debian] (s/split #"-" (:version project))
        debian (if (= "SNAPSHOT" debian)
                 (str "~e" (System/currentTimeMillis))
                 "")
        ]
    (deb-task
     (:description project)
     (let [deb-spec (:deb project)]
       (filter
        (fn [[_ v]] (not (= nil v)))
        (merge
         deb-spec
         {:package (:name project)
          :homepage (:url project)

          :version (version {:epoch (or (:epoch deb-spec) 0)
                             :upstream upstream
                             :debian debian})

          :maintainer (maintainer (:maintainer deb-spec))
          })))

     (tarfileset {:dir "classes"
                  :prefix "/export/disk0"}))))