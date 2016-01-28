(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]]
            [cljsjs.react-bootstrap])
  (:import goog.History))

(def button (reagent/adapt-react-class (aget js/ReactBootstrap "Button")))

(defn home []
  [:div
   [:h2 "A sample title"]
   [button {:bsStyle "primary"} "with a button"]])

(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))

(render-sample)
