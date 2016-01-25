(ns react-cljs.core
  (:require-macros [secretary.core :refer [defroute]])
  (:require [goog.events :as events]
            [goog.dom :as dom]
            [goog.history.EventType :as EventType]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [secretary.core :as secretary]
            [reagent.core :as reagent :refer [atom render]])
  (:import goog.History))

(defn home []
  [:div.pageheader
   [:h2 "Hello World"]])

(defn render-sample []
  (reagent/render-component [home]
                            (.getElementById js/document "app")))


(render-sample)
