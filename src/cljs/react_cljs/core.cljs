(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [reagent.session :as session]
            [react-cljs.allforms :as records]
            [accountant.core :as accountant]
            [cljsjs.react-bootstrap]
            )
  (:import goog.History))


(def button-tool-bar (reagent/adapt-react-class (aget js/ReactBootstrap "ButtonToolbar")))
(def button (reagent/adapt-react-class (aget js/ReactBootstrap "Button")))

(defn show-record [component]
  (reagent/render-component component (.getElementById js/document "record")))


(defn pages []
  [:div
   [:div.page-header
    [:center
     [:h2 "Record Room Management System"]]]
   [:div.col-md-2
    [:div {:class "well"
          :style {:max-width 400 :margin "0 auto 10px"}}
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/masavi")
                             (show-record (records/masavi-table @records/masavi-dataset)))} "Masavi"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/consolidation")
                             (show-record (records/consolidation-table @records/consolidation-dataset)))} "Consolidation"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/fieldform")
                             (show-record (records/fieldform-table @records/fieldform-dataset)))} "Field Form"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/khasragirdwani")
                             (show-record (records/khasragirdwani-table @records/khasragirdwani-dataset)))} "Khasra Girdwani"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/revenuerecord")
                             (show-record (records/revenuerecord-table @records/revenuerecord-dataset)))} "Revenue Record"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/misc")
                             (show-record (records/misc-table @records/misc-dataset)))} "Misc Records"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/o2register")
                             (show-record (records/o2register-table @records/o2register-dataset)))} "O2 Register"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/o4register")
                             (show-record (records/o4register-table @records/o4register-dataset)))} "O4 Register"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/o6register")
                             (show-record (records/o6register-table @records/o6register-dataset)))} "O6 Register"]
     [button {:bs-size "large"
              :block true
              :on-click #(do (accountant/navigate! "#/mutation")
                             (show-record (records/mutation-table @records/mutation-dataset)))} "Mutation Records"]]]
  [:div.col-md-10 {:id "record"}]])


;; -------rendering part---------------
(defn mount-root []
  (reagent/render pages (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!)
  (accountant/dispatch-current!)
  (mount-root))

(init!)
