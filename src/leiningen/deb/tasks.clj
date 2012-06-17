(ns leiningen.deb.tasks
  (:require [lancet.core :as lancet]
            [clojure.java.io :as io])
  (:import (com.googlecode.ant_deb_task Deb$Description)))

(lancet/define-ant-type description com.googlecode.ant_deb_task.Deb$Description)
(lancet/define-ant-type changelog com.googlecode.ant_deb_task.Deb$Changelog)
(lancet/define-ant-type maintainer com.googlecode.ant_deb_task.Deb$Maintainer)
(lancet/define-ant-type priority com.googlecode.ant_deb_task.Deb$Priority)
(lancet/define-ant-type section com.googlecode.ant_deb_task.Deb$Section)
(lancet/define-ant-type tarfileset org.apache.tools.ant.types.TarFileSet)

;; Lancet reflection magic is hopelessly confused by int/long differences
(defn version [{:keys [epoch upstream debian]}]
  (doto (com.googlecode.ant_deb_task.Deb$Version.)
    (.setEpoch epoch)
    (.setUpstream upstream)
    (.setDebian debian)))

(defmethod lancet/coerce [Integer/TYPE Integer] [_ i] (int i))
(defmethod lancet/coerce [Integer/TYPE Long] [_ i] (int i))

(defmethod lancet/coerce [String com.googlecode.ant_deb_task.Deb$Version]
  [_ v] (str v))
(defmethod lancet/coerce [String com.googlecode.ant_deb_task.Deb$Maintainer]
  [_ m] (str m))
(defmethod lancet/coerce [com.googlecode.ant_deb_task.Deb$Section String]
  [_ s] (section {:value s}))
(defmethod lancet/coerce [com.googlecode.ant_deb_task.Deb$Priority String]
  [_ s] (priority {:value s}))

(defmethod lancet/coerce [com.googlecode.ant_deb_task.Deb$Description String]
  [_ s] (doto (description {:synopsis (first (.split s "\n"))})
          (.addText s)))

(.addTaskDefinition lancet/ant-project "deb" com.googlecode.ant_deb_task.Deb)

(defn deb-task
  [project props filesets]
  (let [task (lancet/instantiate-task lancet/ant-project "deb" props)]
    (doseq [fileset filesets]
      (.add task fileset))
    (.setToDir task (io/file (or (:target-path project) (:target-dir project))))
    (.addDescription task (lancet/coerce Deb$Description (:description project)))
    (.execute task)
    task))
