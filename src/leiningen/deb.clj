(ns leiningen.deb
  (:use leiningen.deb.tasks
       [leiningen.jar :only [jar get-jar-filename get-default-jar-name]])
  (:require [clojure.contrib.string :as s]))

(defn- default-deb-spec
  [project]
  {:maintainer {:name "Unknown"
                :email "unknown@example.com"}
   :filesets [{:file (get-jar-filename project)
               :fullpath (str "/usr/share/java/" (get-default-jar-name project))}]
   :package (:name project)
   :homepage (:url project)})

(defn deb
  "Build a debian package of this project."
  [project]
  (let [[upstream debian] (s/split #"-" (:version project))
        ;; SNAPSHOTs should get unix timestamp based 'debian version'
        debian (if (= "SNAPSHOT" debian)
                 (str "~e" (System/currentTimeMillis))
                 "")
        deb-spec (merge (default-deb-spec project)
                        (:deb project))]
    (when-not (:deb-skip-jar project) (jar project))
    (apply deb-task
           (:description project)
           (into
            {}
            (filter
             (fn [[_ v]] (not (nil? v)))

             (select-keys
              (merge
               deb-spec
               {:version (version {:epoch (or (:epoch deb-spec) 0)
                                   :upstream upstream
                                   :debian debian})
                :maintainer (maintainer (:maintainer deb-spec))})
              [:toDir :debFilename :package :version :section
               :priority :architecture :depends :preDepends
               :recommends :suggests :enhances :conflicts
               :provides :replaces :maintainer :homepage
               :preinst :postinst :prerm :postrm :config
               :templates :triggers])))

           (map #(tarfileset %) (:filesets deb-spec)))))
