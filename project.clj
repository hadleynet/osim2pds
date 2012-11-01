(defproject osim2pds "0.1.0-SNAPSHOT"
  :description "Modest script to export data from the OSIM2 format to that expected by the patient
data server."
  :url "http://github.com/hadleynet/osim2pds"
  :license {
    :name "Apache 2.0"
    :url "http://www.apache.org/licenses/LICENSE-2.0"
  }
  :dependencies [
    [org.clojure/clojure "1.4.0"]
    [korma "0.3.0-beta7"]
    [com.oracle/ojdbc6 "11.2.0.3"]
  ]
  :repositories [
    ["java.net" "http://download.java.net/maven/2"]
  ]
  :main osim2pds.core
)
