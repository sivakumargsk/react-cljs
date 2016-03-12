(ns react-cljs.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [reagent.core :as reagent]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]))
(def initial-state {:click-count 0
                    :value "foo"
                    :tasks {1 {:task-name "Learning Re-frame Framework" :done false}
                            2 {:task-name "Implementing Reagent Examples on re-frame" :done false}
                            3 {:task-name "Doing To-Do List" :done false}}
                    })

;; Event handler ------------------------

(register-handler
 :initialize
 (fn
   [db _]
   (merge db initial-state)))

(register-handler
 :change-click-count
 (fn
   [db [_ value]]
   (assoc db :click-count value)))

(register-handler
 :change-value
 (fn [db [_ newvalue]]
   (assoc db :value newvalue)))

(register-handler
 :add-task
 (fn [db [_ key name]]
   (assoc-in db [:tasks key] {:task-name name :done false})))

(register-handler
 :task-done
 (fn
   [db [_ key]]
   (update-in db [:tasks key :done] not)))

(register-handler
 :remove-task
 (fn
   [db [_ id]]
   (update-in db [:tasks] dissoc id)))

;; Subscription Handlers -------------------

(register-sub
 :count
 (fn
   [db _]
   (reaction (:click-count @db))))

(register-sub
 :value
 (fn
   [db _]
   (reaction (:value @db))))

(register-sub
 :tasks
 (fn [db _]
   (reaction (:tasks @db))))

;; View Components -------------------------

(defn counting-component []
  (let [val (subscribe [:count])]
    (fn
      []
      [:div.form-group
       [:label "The atom Click Count Has Value: " @val]
       [:br]
       [:button {:on-click #(dispatch [:change-click-count (swap! val inc)])} "Click Me"]])))

(defn input-changing []
  (let [val (subscribe [:value])]
    (fn []
      [:div.form-group
       [:label "The value is now: " @val]
       [:br]
       [:label "Change Here "
        [:input {:type "text"
                 :value @val
                 :on-change #(dispatch [:change-value (reset! val (-> % .-target .-value))])}]]])))

(defn max-coll [coll]
  (if (empty? coll)
    0
    (reduce (fn [x y]
              (if (> x y) x y)) coll)))


(defn get-val [id]
  (.-value (dom/getElement id)))

(defn set-val [id val]
  (set! (.-value (dom/getElement id)) val))


(defn to-do []
  (let [tasks (subscribe [:tasks])]
    (fn []
      (let [task-no (inc (max-coll (keys @tasks)))]
        [:div
         [:div.form-group
          [:div.row
           [:div.col-sm-8
            [:input.form-control {:id "add"
                                  :type "text"
                                  :placeholder "Enter Your tasks Here"}]]
           [:div.col-sm-2
            [:button.btn.btn-primary {:on-click  #(do
                                                    (dispatch [:add-task task-no (get-val "add")])
                                                    (set-val "add" "")) } "Add New Task"]]]]
         (for [t @tasks]
           ^{:key (first t)}
           [:div.form-group
            [:div.row
             [:div.col-sm-1
              [:input
               {:id (first t)
                :type "checkbox"
                :style {:width "25px" :height "25px"}
                :on-change #(dispatch [:task-done (js/parseInt (-> % .-target .-id))])}]]
             [:div.col-sm-8
              [:strong (when (:done (second t))
                         {:style {:color "green"}})
               (:task-name (second t))]]
             [:div.col-sm-1
              [:button.btn.btn-danger
               {:id (first t)
                :on-click #(dispatch [:remove-task (js/parseInt (-> % .-target .-id)) ])}
               "Delete"]]]])]))))

(defn reagent-examples []
  [:div.container
   [:div.page-header [:h2 "Reagent Examples In Re-frame"]]
   [counting-component]
   [:hr]
   [input-changing]
   [:hr]
   [to-do]])

(defn run
  []
  (dispatch-sync [:initialize])
  (reagent/render [reagent-examples]
                  (js/document.getElementById "app")))
(run)


