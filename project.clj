(defproject lein-deb "1.0.0"
  :description "A leiningen plugin for building debs"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [tvachon/ant-deb-task "0.1.1"]
                 [org.apache.ant/ant "1.9.4"]
                 [lancet "1.0.2"]]
  :eval-in-leiningen true)
