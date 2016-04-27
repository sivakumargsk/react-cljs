(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [clojure.string :as str])
  (:import goog.History))

(def jquery (js* "$"))

(def my-atom (reagent/atom
              {:rooms {1 {:name "Single" :rooms 1 :nights 10 :hotelname "Super" :amount 200 }
                       2 {:name "Single" :rooms 1 :nights 10 :hotelname "Super" :amount 300 }}}))
;; Add Input elements

(defn add-text [label id type data]
  [:div.col-sm-2
   [:label.sr-only label]
   [:input.form-control
    {:id id
     :type type
     :value (@data id)
     :placeholder label
     :on-change #(swap! data assoc id (-> % .-target .-value))}]])

(defn add-num [label id type data]
  [:div.col-sm-2
   [:label.sr-only label]
   [:input.form-control
    {:id id
     :type type
     :value (@data id)
     :placeholder label
     :on-change #(swap! data assoc id (int (-> % .-target .-value)))}]])

(defn add-select [id data]
  [:div.col-sm-2
   [:select.form-control
    {:id id
     :value (@data id 0)
     :on-change #(swap! data
                        assoc
                        id
                        (-> % .-target .-value))}
    [:option {:value 0} "Room Type"]
    (for [d ["Single" "Double" "Triple" "Child with bed" "Quad Occupancy"]]
      ^{:key d}
      [:option {:value d} d])]])


(defn datepicker [id data]
  (reagent/create-class
   {:component-did-mount
    (fn [this]
      (.val  (jquery (str "#" (name id))) (@data id))
      (.datepicker (jquery (str "#" (name id))) (clj->js {:format "dd/mm/yyyy"
                                                          :autoclose true
                                                          :todayBtn  true
                                                          :todayHighlight true} ))
      (.on (.datepicker (jquery (str "#" (name id))))
           "changeDate" #(swap! data assoc id  (.val (jquery (str "#" (name id)))))))
    :reagent-render
    (fn []
      [:div.input-group.date
       [:input.form-control {:id id
                             :type "text"
                             :placeholder "dd/mm/yyyy"}]
       [:div.input-group-addon [:span.glyphicon.glyphicon-th]]])}))


;; -------------------------------------------------------------------------

(defn upd-text [label id type num data]
  [:div.col-sm-2
   [:label.sr-only label]
   [:input.form-control
    {:id id
     :type type
     :value (get-in @data [:rooms num id])
     :placeholder label
     :on-change #(swap! data assoc-in [:rooms num id]
                        (-> % .-target .-value))}]])

(defn upd-num [label id type num data]
  [:div.col-sm-2
   [:label.sr-only label]
   [:input.form-control
    {:id id
     :type type
     :value (get-in @data [:rooms num id])
     :placeholder label
     :on-change #(swap! data assoc-in [:rooms num id]
                        (int (-> % .-target .-value)))}]])

(defn upd-select [id num data]
  [:div.col-sm-2
   [:select.form-control
    {:id id
     :value (get-in @data [:rooms num id])
     :on-change #(swap! data
                        assoc-in
                        [:rooms num id]
                        (-> % .-target .-value))}
    [:option {:value 0} "Room Type"]
    (for [d ["Single" "Double" "Triple" "Child with bed" "Quad Occupancy"]]
      ^{:key d}
      [:option {:value d} d])]])

(defn add-accom-comp [ratom]
  (let [data (reagent/atom {:rooms 1
                            :date "2/2/2015"})]
    (fn []
      [:div
       [:div.row
        [add-select :name data]
        [add-num "No of Rooms" :rooms "text" data]
        [add-num "No of Nights" :nights "text" data]
        [add-text "Hotel name" :hotelname "text" data]
        [add-num "Amount" :amount "text" data]
        [datepicker :date data]
        [:button.btn.btn-info
         {:on-click #((swap! ratom
                             assoc-in
                             [:rooms
                              (if (nil? (keys (@ratom :rooms))) 1
                                  (inc (apply max (keys (@ratom :rooms)))))]
                             @data)
                      (reset! data {:rooms 1}))}
         [:span.glyphicon.glyphicon-plus]]]
       [:p (str @data)]])))

(defn  accommodation-comp [ratom]
  [:div
   (for [i (@ratom :rooms)]
     ^{:key (first i)}
     [:div.fomr-group
      [:div.row
       [upd-select :name (first i) ratom]
       [upd-num "No of Rooms" :rooms "text" (first i) ratom]
       [upd-num "No of Nights" :nights "text" (first i) ratom]
       [upd-text "Hotel name" :hotelname "text" (first i) ratom]
       [upd-num  "Amount" :amount "text" (first i) ratom]
       [:button.btn.btn-danger
        {:on-click #(swap! ratom
                           update-in [:rooms] dissoc (first i))}
        [:span.glyphicon.glyphicon-remove]]]])
   [:p (str @ratom)]
   [add-accom-comp ratom]])

(defn home []
  [:div
   [:div.page-header [:h2 "Rooms Form"]]
   [accommodation-comp my-atom]])

;; ---------------------------------
;; rendering-part
(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))

(render-sample)
