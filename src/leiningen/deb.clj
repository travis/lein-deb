(ns leiningen.deb
  (:use
      [leiningen.deb.tasks]
      [leiningen.jar :only [jar get-jar-filename]])
  (:require
      [clojure.pprint :as pp]
      [clojure.string :as s]))

(defn- default-deb-spec
  [project]
  {:maintainer {:name "Unknown"
                :email "unknown@example.com"}
   :filesets []
   :package (:name project)
   :homepage (:url project)})

(defn deb
  "Build a debian package of this project."
  [project]
  (let [[upstream debian] (s/split (:version project) #"-")
        ;; SNAPSHOTs should get unix timestamp based 'debian version'
        debian (if (= "SNAPSHOT" debian)
                 (str "~e" (System/currentTimeMillis))
                 "")
        deb-spec (merge (default-deb-spec project)
                        (:deb project))]

    (pp/pprint deb-spec)

    (apply deb-task
      (:description project)
      (into {}
         (filter
          (fn [[_ v]] (not (nil? v)))

          (select-keys
           (merge
            deb-spec
            {:version (version {:epoch (int (or (:epoch deb-spec) 0))
                                :upstream (or (:upstream deb-spec) upstream)
                                :debian (or (:debian deb-spec) debian)})
             :maintainer (maintainer (:maintainer deb-spec))})
           [:toDir
            :debFilename
            :package
            :version
            :section
            :priority
            :architecture
            :depends
            :preDepends
            :recommends
            :suggests
            :enhances
            :conflicts
            :provides
            :replaces
            :maintainer
            :homepage
            :preinst
            :postinst
            :prerm
            :postrm
            :config
            :templates
            :triggers])))

         (map #(tarfileset %) (:filesets deb-spec)))))
