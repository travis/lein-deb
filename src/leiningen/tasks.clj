(ns leiningen.tasks
  (use lancet)
  (require [clojure.contrib.string :as s]))

(define-ant-type description com.googlecode.ant_deb_task.Deb$Description)
(define-ant-type changelog com.googlecode.ant_deb_task.Deb$Changelog)
(define-ant-type maintainer com.googlecode.ant_deb_task.Deb$Maintainer)
(define-ant-type priority com.googlecode.ant_deb_task.Deb$Priority)
(define-ant-type section com.googlecode.ant_deb_task.Deb$Section)
(define-ant-type version com.googlecode.ant_deb_task.Deb$Version)
(define-ant-type tarfileset org.apache.tools.ant.types.TarFileSet)

(defmethod coerce [Integer/TYPE Integer] [_ i] (int i))
(defmethod coerce [String com.googlecode.ant_deb_task.Deb$Version]
  [_ v] (str v))
(defmethod coerce [String com.googlecode.ant_deb_task.Deb$Maintainer]
  [_ m] (str m))
(defmethod coerce [com.googlecode.ant_deb_task.Deb$Section String]
  [_ s] (section {:value s}))
(defmethod coerce [com.googlecode.ant_deb_task.Deb$Priority String]
  [_ s] (priority {:value s}))

(defmethod coerce [com.googlecode.ant_deb_task.Deb$Description String]
  [_ s] (doto (description {:synopsis (first (s/split-lines s))})
          (.addText s)))

(.addTaskDefinition ant-project "deb" com.googlecode.ant_deb_task.Deb)

(defn deb-task
  [description props & filesets]
  (let [task (instantiate-task ant-project "deb" props)]
    (doseq [fileset filesets]
      (.add task fileset))
    (.addDescription task (coerce com.googlecode.ant_deb_task.Deb$Description description))
    (.execute task)
    task) )


