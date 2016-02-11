(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [clojure.string :as str])
  (:import goog.History))

;; ---------------------------------------------
;; CSS
(def p-style {:font-size "20px"
              :font-weight "bold"
              :color "grey"
              :font-style "italic"
              :font-family {:align "center"}})

(def p1-style {:font-size "20px"
               :font-weight "bold"
               :color "green"
               :font-style "italic"
               :font-family {:align "center"}})

;; --------------------------------------------
;; global-atom

(def todo-list  (atom  [{:task-no 1 :task-name "Learning Clojure From Ground up" :done false}
                        {:task-no 2 :task-name "Four Clojure 1 to 10 Tasks" :done false}
                        {:task-no 3 :task-name "Learning bash Commands" :done false}]))

(defn get-val [id]
  (.-value (dom/getElement id)))

(defn set-val [id val]
  (set! (.-value (dom/getElement id)) val))

(defn head [fun]
  [:div.form-group
   [:div.row
    [:div.col-sm-10 [:input.form-control {:type "text" :id "add" :placeholder "Enter your tasks here"}]]
    [:div.col-sm-2 [:button.btn.btn-primary {:on-click fun} "Add"]]]])

(defn add-onclick []
  (when-not (str/blank? (get-val "add"))
    (do (swap! todo-list conj
               {:task-no (inc (:task-no (last @todo-list)))
                :task-name (get-val"add")
                :done false})
        (set-val "add" ""))))

(defn rm-task [num coll]
  (reduce (fn [x y]
            (if (= num (:task-no y))
              x
              (conj x y))) [] coll))

(defn cb-onclick [x coll]
  (let [idx (loop [c @coll
                   ans 0]
              (if (= x (:task-no (first c)))
                ans
                (recur (next c) (inc ans))))]
    (swap! coll update-in [idx :done] not)))

(defn task-text [map]
  [:div.col-sm-9 (if (:done map)
                   [:p {:style p1-style}  (:task-name map)]
                   [:p {:style p-style}  (:task-name map)])])

(defn tasks-list []
  [:div
   (for [task @todo-list]
     ^{:key (:task-no task)}
     [:div.row
      [:div.col-sm-1 [:input {:id (:task-no task) :type "checkbox" :style {:width "25px" :height "25px"} :on-change #(cb-onclick (js/parseInt (-> % .-target .-id)) todo-list)}]]
      [:div.col-sm-9 (if (:done task)
                       [:p {:style p1-style}  (:task-name task)]
                       [:p {:style p-style}  (:task-name task)])]
      [:div.col-sm-2 [:button.btn.btn-default {:id (:task-no task) :on-click #(reset! todo-list (rm-task (js/parseInt (-> % .-target .-id)) @todo-list))}
                      [:span.glyphicon.glyphicon-remove {:aria-hidden true}]]]])])

(defn home []
  [:div.container
   [:div.page-header [:h2 "To Do List"]]
    [head add-onclick]
    [:div
     [:hr]
     [tasks-list]
     [:hr]]
    [:span (str @todo-list)]])



;; ---------------------------------
;; rendering-part
(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))

(render-sample)


