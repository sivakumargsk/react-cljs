(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [goog.net.XhrIo :as xhr])
  (:import goog.History))


;; (def dist (js->clj [{"id":1,"name":"East"},{"id":2,"name":"North"},{"id":3,"name":"North East"},{"id":4,"name":"North West"},{"id":5,"name":"South"},{"id":6,"name":"South West"},{"id":7,"name":"West"}]))


;; [{"id":1,"districtid":1,"subdivionname":"Gandhi Nagar"},{"id":2,"districtid":1,"subdivionname":"Preet Vihar"},{"id":3,"districtid":1,"subdivionname":"Vivek Vihar"}]

;; [{"id":1,"districtid":1,"subdivionname":"Gandhi Nagar"},{"id":2,"districtid":1,"subdivionname":"Preet Vihar"},{"id":3,"districtid":1,"subdivionname":"Vivek Vihar"},{"id":4,"districtid":2,"subdivionname":"Civil Lines"},{"id":5,"districtid":2,"subdivionname":"Sadar Bazar"},{"id":6,"districtid":3,"subdivionname":"Sealam Pur"},{"id":7,"districtid":3,"subdivionname":"Shahdara"},{"id":8,"districtid":4,"subdivionname":"Model Town"},{"id":9,"districtid":4,"subdivionname":"Narela"},{"id":10,"districtid":4,"subdivionname":"Saraswati Vihar"},{"id":11,"districtid":5,"subdivionname":"Defence Colony"},{"id":12,"districtid":5,"subdivionname":"Hauz Khas"},{"id":13,"districtid":5,"subdivionname":"Kalkaji"},{"id":14,"districtid":6,"subdivionname":"Delhi Cant"},{"id":15,"districtid":6,"subdivionname":"Najafgarh"},{"id":16,"districtid":6,"subdivionname":"Vasant Vihar"},{"id":17,"districtid":7,"subdivionname":"Patel Nagar"},{"id":18,"districtid":7,"subdivionname":"Punjabi Bagh"}]


;; [{"id":1,"subdivisionid":1,"villagename":"Shakar Pur Brewad"},{"id":2,"subdivisionid":1,"villagename":"Shakar Pur Khasi"}]

(def dist [{:id 1 :name "East"}
           {:id 7 :name "West"}
           {:id 2 :name "North"}
           {:id 5 :name "South"}
           {:id 3 :name "North East"}
           {:id 4 :name "North West"}
           {:id 6 :name "South West" }])


(def subdivision (atom nil))

(defn http-get [url callback]
  (xhr/send url callback))

(defn getdata [res]
  (.getResponseJson (.-target res)))

(defn dist-select [id]
  (let [res (fn [json]
              (let [dt (getdata json)]
                (reset! subdivision dt)))]
    (http-get (str  "http://localhost:9000/districts/" id  "/subdivisions") res)))

(defn district-select-tag [id data]
  [:select.form-control {:id id
            :on-change #(dist-select (-> % .-target .-value)) }
   [:option {:value 0} "--Select--"]
   (for [d  data]
     ^{:key (:id d) }
     [:option {:value (:id d)} (:name d)])])


(defn subdivision-select-tag [id]
  [:select.form-control {:id id }
   [:option {:value 0} "--Select--"]
   (for [d @subdivision]
     ^{:key (.-id d) }
     [:option {:value (.-id d)} (.-subdivionname d)])])


(defn home []
  [:div
   [district-select-tag :districts dist]
   [subdivision-select-tag :subdivision ]])

(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))


(render-sample)
