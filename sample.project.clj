(defproject lein-deb-test "3.14.7"
  :description "Example project illustrating a leiningen debian plugin"

  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ; plugins list
  :plugins [[lein-deb "1.0.0"]]

  :dependencies [[org.clojure/clojure "1.6.0"]]

  ;; Self sufficient section independent of java/jar/clojure etc.
  :deb {
   :maintainer {:email "unknown@example.com",
                :name "Dr. Who"}

   ; debian version (optional)
   ; Refer: http://www.fifi.org/doc/debian-policy/policy.html/ch-versions.html
   ; (epoch defaults to zero,
   ;  upstream defaults to using project version
   ;  debian defaults to empty string (for SNAPSHOT uses timestamp)
   ; :epoch 1 :upstream "3.20.7" :debian "test"

   ; Name of the package (optional, defaults to  using project-name)
   :package "lein-deb-test"

   ; FileSets specification (indicate what to include/exclude)
   ; Refer:https://ant.apache.org/manual/Types/tarfileset.html
   ;       https://ant.apache.org/manual/Types/fileset.html
   ; for more examples
   :filesets
      [{:file "target/lein-deb-test*.jar",
        :fullpath "/opt/my-example-service/myservice.jar"}
       {:includes "**/*",
        :dir "target/classes/"
        :prefix "/opt/my-example-service/classes/"}]})
