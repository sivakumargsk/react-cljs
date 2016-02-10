(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]])
  (:import goog.History))


(def p-style {:font-size "20px"
              :font-weight "bold"
              :color "grey"
              :font-style "italic"
              :font-family {:align "center"}
              :text-decoration "line-through"})


(def todo-list  (atom  [{:task-no 1 :task-name "Learning Clojure From Ground up" :done false}
                     {:task-no 2 :task-name "Four Clojure 1 to 10 Tasks" :done false}
                     {:task-no 3 :task-name "Learning bash Commands" :done false}]))

(defn tasks-list []
  [:div
  (for [item @todo-list]
    ^{:key (:task-no item)}
    [:div.row
     [:div.col-sm-1 [:input {:type "checkbox" :style {:width "25px" :height "25px"}}]]
     [:div.col-sm-9 [:p {:style p-style}  (:task-name item)]]
     [:div.col-sm-2 [:button.btn.btn-default
                     [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]]])])

(defn get-input-text[id]
  (.-value (.getElementById js/document id)))

(defn add []
  (swap! todo-list conj {:task-no (inc (:task-no (last @todo-list)))
                         :task-name (get-input-text "app")
                         :done false}))

(defn head [fun]
  [:div.form-group
   [:div.row
     [:div.col-sm-10 [:input.form-control {:type "text" :id "add" }]]
     [:div.col-sm-2 [:button.btn.btn-primary {:on-click fun} "Add"]]]])

(defn home []
  [:div.container
   [:div.col-sm-8
    [:div.page-header [:h2 "To Do List"]]
    [head add]
    [:div
     [:hr]
     [tasks-list]
     [:hr]]
    [:span (str @todo-list)]]])

(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))


(render-sample)


