(ns leiningen.deb
  (:use [leiningen.deb.tasks :only [deb-task version maintainer tarfileset]]
        [leiningen.jar :only [jar get-jar-filename]])
  (:require [clojure.string :as s]))

(defn- default-deb-spec
  [project]
  (let [full-path (get-jar-filename project)
        basename (.getName (java.io.File. full-path))]
    {:maintainer {:name "Unknown"
                  :email "unknown@example.com"}
     :filesets [{:file full-path
                 :fullpath (str "/usr/share/java/" basename)}]
     :package (:name project)
     :homepage (:url project)}))

(def valid-keys [:toDir :debFilename :package :version :section
                 :priority :architecture :depends :preDepends
                 :recommends :suggests :enhances :conflicts
                 :provides :replaces :maintainer :homepage
                 :preinst :postinst :prerm :postrm :config
                 :templates :triggers])

(defn deb
  "Build a debian package of this project."
  [project]
  (let [[upstream debian] (s/split (:version project) #"-")
        ;; SNAPSHOTs should get unix timestamp based 'debian version'
        debian (if (= "SNAPSHOT" debian)
                 (str "~e" (System/currentTimeMillis))
                 "")
        deb-spec (merge (default-deb-spec project)
                        (:deb project))
        props (-> deb-spec
                  (merge {:version (version {:epoch (:epoch deb-spec 0)
                                             :upstream upstream
                                             :debian debian})
                          :maintainer (maintainer (:maintainer deb-spec))})
                  (select-keys valid-keys))]
    (when-not (:deb-skip-jar project) (jar project))
    (deb-task project
              (into {} (filter val props))
              (map tarfileset (:filesets deb-spec)))))
