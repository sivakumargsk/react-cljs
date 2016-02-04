(ns react-cljs.allforms
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [bouncer.core :as b]
            [bouncer.validators :as v]
            [accountant.core :as accountant]))



;; -------------------------
;; input-elements for all records

(defn input-element [id ttype data-set placeholder in-focus]
  [:input.form-control {:id id
                        :type ttype
                        :value (@data-set id)
                        :placeholder placeholder
                        :on-change #(swap! data-set assoc id (-> % .-target .-value))
                        :on-blur  #(reset! in-focus "on")
                        }])


;; -------------------------
;; masavi forms

(def masavi-dataset (atom [{:id 1
                            :s-no 101
                            :village-name "pooram"
                            :title "house-sales"
                            :tehsil "Appa Rao"
                            :year 2008
                            :rack-location "A"}
                           {:id 2
                            :s-no 102
                            :village-name "ramapuram"
                            :title "house-tax"
                            :tehsil "Rama Rao"
                            :year 2012
                            :rack-location "B"}]))


(defn masavi-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/masavi/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/masavi/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn masavi-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :s-no [[v/required :message "field is required"]]
                     :village-name [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :tehsil [[v/required :message "field is required"]]
                     :year [[v/required :message "field is required"]]
                     :rack-location [[v/required :message "field is required"]])))

(defn masavi-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (masavi-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((masavi-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn masavi-save-onclick [data-set focus]
  (if (= nil (masavi-validator @data-set))
    (do
      (swap! masavi-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn masavi-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [masavi-row :id "Id" "number" data-set focus]
       [masavi-row :s-no "S.No" "number" data-set focus]
       [masavi-row :village-name "Village Name" "text" data-set focus ]
       [masavi-row :title "Title" "text" data-set focus]
       [masavi-row :tehsil "Tehsil" "text" data-set focus]
       [masavi-row :year "Year" "date" data-set focus]
       [masavi-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn masavi-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [masavi-document-template
       "New Document"
       add-data focus
       #(masavi-save-onclick add-data focus)])))

(defn masavi-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [masavi-document-template
       "Update Document"
       update-data focus
       #(masavi-save-onclick update-data focus)])))

;; --------------------------------
;; Consolidation form Record

(def consolidation-dataset (atom [{:id 1
                                   :s-no 101
                                   :village-name "pooram"
                                   :title "house-sales"
                                   :tehsil "Appa Rao"
                                   :year 2008
                                   :rack-location "A"}
                                  {:id 2
                                   :s-no 102
                                   :village-name "ramapuram"
                                   :title "house-tax"
                                   :tehsil "Rama Rao"
                                   :year 2012
                                   :rack-location "B"}]))


(defn consolidation-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/consolidation/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/consolidation/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn consolidation-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :s-no [[v/required :message "field is required"]]
                     :village-name [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :tehsil [[v/required :message "field is required"]]
                     :year [[v/required :message "field is required"]]
                     :rack-location [[v/required :message "field is required"]])))

(defn consolidation-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (consolidation-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((consolidation-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn consolidation-save-onclick [data-set focus]
  (if (= nil (consolidation-validator @data-set))
    (do
      (swap! consolidation-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn consolidation-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [consolidation-row :id "Id" "number" data-set focus]
       [consolidation-row :s-no "S.No" "number" data-set focus]
       [consolidation-row :village-name "Village Name" "text" data-set focus ]
       [consolidation-row :title "Title" "text" data-set focus]
       [consolidation-row :tehsil "Tehsil" "text" data-set focus]
       [consolidation-row :year "Year" "date" data-set focus]
       [consolidation-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn consolidation-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [consolidation-document-template
       "New Document"
       add-data focus
       #(consolidation-save-onclick add-data focus)])))

(defn consolidation-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [consolidation-document-template
       "Update Document"
       update-data focus
       #(consolidation-save-onclick update-data focus)])))


;; --------------------------------
;; fieldform form Record

(def fieldform-dataset (atom [{:id 1
                               :s-no 101
                               :village-name "pooram"
                               :title "house-sales"
                               :tehsil "Appa Rao"
                               :year 2008
                               :rack-location "A"}
                              {:id 2
                               :s-no 102
                               :village-name "ramapuram"
                               :title "house-tax"
                               :tehsil "Rama Rao"
                               :year 2012
                               :rack-location "B"}]))


(defn fieldform-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/fieldform/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/fieldform/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn fieldform-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :s-no [[v/required :message "field is required"]]
                     :village-name [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :tehsil [[v/required :message "field is required"]]
                     :year [[v/required :message "field is required"]]
                     :rack-location [[v/required :message "field is required"]])))

(defn fieldform-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (fieldform-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((fieldform-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn fieldform-save-onclick [data-set focus]
  (if (= nil (fieldform-validator @data-set))
    (do
      (swap! fieldform-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn fieldform-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [fieldform-row :id "Id" "number" data-set focus]
       [fieldform-row :s-no "S.No" "number" data-set focus]
       [fieldform-row :village-name "Village Name" "text" data-set focus ]
       [fieldform-row :title "Title" "text" data-set focus]
       [fieldform-row :tehsil "Tehsil" "text" data-set focus]
       [fieldform-row :year "Year" "date" data-set focus]
       [fieldform-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn fieldform-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [fieldform-document-template
       "New Document"
       add-data focus
       #(fieldform-save-onclick add-data focus)])))

(defn fieldform-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [fieldform-document-template
       "Update Document"
       update-data focus
       #(fieldform-save-onclick update-data focus)])))

;; --------------------------------
;; khasragirdwani form Record

(def khasragirdwani-dataset (atom [{:id 1
                                    :s-no 101
                                    :village-name "pooram"
                                    :title "house-sales"
                                    :tehsil "Appa Rao"
                                    :year 2008
                                    :rack-location "A"}
                                   {:id 2
                                    :s-no 102
                                    :village-name "ramapuram"
                                    :title "house-tax"
                                    :tehsil "Rama Rao"
                                    :year 2012
                                    :rack-location "B"}]))


(defn khasragirdwani-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/khasragirdwani/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/khasragirdwani/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn khasragirdwani-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :s-no [[v/required :message "field is required"]]
                     :village-name [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :tehsil [[v/required :message "field is required"]]
                     :year [[v/required :message "field is required"]]
                     :rack-location [[v/required :message "field is required"]])))

(defn khasragirdwani-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (khasragirdwani-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((khasragirdwani-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn khasragirdwani-save-onclick [data-set focus]
  (if (= nil (khasragirdwani-validator @data-set))
    (do
      (swap! khasragirdwani-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn khasragirdwani-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [khasragirdwani-row :id "Id" "number" data-set focus]
       [khasragirdwani-row :s-no "S.No" "number" data-set focus]
       [khasragirdwani-row :village-name "Village Name" "text" data-set focus ]
       [khasragirdwani-row :title "Title" "text" data-set focus]
       [khasragirdwani-row :tehsil "Tehsil" "text" data-set focus]
       [khasragirdwani-row :year "Year" "date" data-set focus]
       [khasragirdwani-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn khasragirdwani-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [khasragirdwani-document-template
       "New Document"
       add-data focus
       #(khasragirdwani-save-onclick add-data focus)])))

(defn khasragirdwani-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [khasragirdwani-document-template
       "Update Document"
       update-data focus
       #(khasragirdwani-save-onclick update-data focus)])))

;; --------------------------------
;; revenuerecord form Record

(def revenuerecord-dataset (atom [{:id 1
                                   :s-no 101
                                   :village-name "pooram"
                                   :title "house-sales"
                                   :tehsil-subdivision "Appa Rao"
                                   :year 2008
                                   :rack-location "A"}
                                  {:id 2
                                   :s-no 102
                                   :village-name "ramapuram"
                                   :title "house-tax"
                                   :tehsil-subdivision "Rama Rao"
                                   :year 2012
                                   :rack-location "B"}]))


(defn revenuerecord-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/revenuerecord/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "S.No"]
       [:th "Village Name"]
       [:th "Title"]
       [:th "Tehsil/Sub-Division"]
       [:th "Year"]
       [:th "Rack Location"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :s-no)]
         [:td (doc :village-name)]
         [:td (doc :title)]
         [:td (doc :tehsil-subdivision)]
         [:td (doc :year)]
         [:td (doc :rack-location)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/revenuerecord/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn revenuerecord-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :s-no [[v/required :message "field is required"]]
                     :village-name [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :tehsil-subdivision [[v/required :message "field is required"]]
                     :year [[v/required :message "field is required"]]
                     :rack-location [[v/required :message "field is required"]])))

(defn revenuerecord-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (revenuerecord-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((revenuerecord-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn revenuerecord-save-onclick [data-set focus]
  (if (= nil (revenuerecord-validator @data-set))
    (do
      (swap! revenuerecord-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn revenuerecord-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [revenuerecord-row :id "Id" "number" data-set focus]
       [revenuerecord-row :s-no "S.No" "number" data-set focus]
       [revenuerecord-row :village-name "Village Name" "text" data-set focus ]
       [revenuerecord-row :title "Title" "text" data-set focus]
       [revenuerecord-row :tehsil-subdivision "Tehsil/Sub-Division" "text" data-set focus]
       [revenuerecord-row :year "Year" "date" data-set focus]
       [revenuerecord-row :rack-location "Rack Location" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn revenuerecord-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [revenuerecord-document-template
       "New Document"
       add-data focus
       #(revenuerecord-save-onclick add-data focus)])))

(defn revenuerecord-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [revenuerecord-document-template
       "Update Document"
       update-data focus
       #(revenuerecord-save-onclick update-data focus)])))


;; --------------------------------
;; misc form Record

(def misc-dataset (atom [{:id 1
                          :file-number 101
                          :subject "pooram"
                          :title "house-sales"
                          :remarks "Appa Rao"
                          :dispatched-date "9-9-1992"
                          :received-date "10-9-2012"}
                         {:id 2
                          :file-number 102
                          :subject "ramapuram"
                          :title "house-tax"
                          :remarks "Rama Rao"
                          :dispatched-date "9-8-2012"
                          :received-date "9-9-2012"}]))


(defn misc-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/misc/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "File Number"]
       [:th "Subject"]
       [:th "Title"]
       [:th "Remarks"]
       [:th "Dispatched Date"]
       [:th "Received Date"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :file-number)]
         [:td (doc :subject)]
         [:td (doc :title)]
         [:td (doc :remarks)]
         [:td (doc :dispatched-date)]
         [:td (doc :received-date)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/misc/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn misc-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :file-number [[v/required :message "field is required"]]
                     :subject [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :remarks [[v/required :message "field is required"]]
                     :dispatched-date [[v/required :message "field is required"]]
                     :received-date [[v/required :message "field is required"]])))

(defn misc-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (misc-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((misc-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn misc-save-onclick [data-set focus]
  (if (= nil (misc-validator @data-set))
    (do
      (swap! misc-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn misc-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [misc-row :id "Id" "number" data-set focus]
       [misc-row :file-number "File Number" "text" data-set focus]
       [misc-row :subject "Subject" "text" data-set focus ]
       [misc-row :title "Title" "text" data-set focus]
       [misc-row :remarks "Remarks" "text" data-set focus]
       [misc-row :dispatched-date "Dispatched Date" "date" data-set focus]
       [misc-row :recevied-date "Recevied Date" "date" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn misc-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [misc-document-template
       "New Document"
       add-data focus
       #(misc-save-onclick add-data focus)])))

(defn misc-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [misc-document-template
       "Update Document"
       update-data focus
       #(misc-save-onclick update-data focus)])))


;;----------------------------------
;; o2 register

(def o2register-dataset (atom [{:id 1
                                :tehsil "gvk"
                                :s-no 101
                                :date-of-institution " 12/05/1991"
                                :source-of-receipt "house-sales"
                                :name-of-village "kolanupaka"
                                :name-of-the-partys "tdp"
                                :date-of-receipt-from-panchayat "09/12/2015"
                                :date-and-gist-of-final-order "05/08/1998"
                                :rack-location "sda"
                                :starting-year-of-o2-register 1997
                                :ending-year-of-o2-register 2000}]))


(defn o2register-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :tehsil [[v/required :message "field is required"]]
                     :s-no [[v/required :message "field is required"]]
                     :date-of-institution [[v/required :message "field is required"]]
                     :source-of-receipt [[v/required :message "field is required"]]
                     :name-of-village [[v/required :message "field is required"]]
                     :name-of-the-partys [[v/required :message "field is required"]]
                     :date-of-receipt-from-panchayat [[v/required :message "field is required"]]
                     :date-and-gist-of-final-order [[v/required :message "field is required"]]
                     :rack-location [[v/required :message "field is required"]]
                     :starting-year-of-o2-register [[v/required :message "field is required"]]
                     :ending-year-of-o2-register [[v/required :message "field is required"]])))

(defn o2register-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/o2register/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "Tehsil"]
       [:th "S.no"]
       [:th "Date Of institution"]
       [:th "Source of receipt"]
       [:th "Name of village"]
       [:th "Name of the partys"]
       [:th "Date of receipt from panchayat"]
       [:th "Date and gist of final order "]
       [:th "rack location"]
       [:th "Starting year of 02 Register"]
       [:th "Ending year of 02 Register"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :tehsil)]
         [:td (doc :s-no)]
         [:td (doc :date-of-institution)]
         [:td (doc :source-of-receipt)]
         [:td (doc :name-of-village)]
         [:td (doc :name-of-the-partys)]
         [:td (doc :date-of-receipt-from-panchayat)]
         [:td (doc :date-and-gist-of-final-order)]
         [:td (doc :rack-location)]
         [:td (doc :starting-year-of-o2-register)]
         [:td (doc :ending-year-of-o2-register)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/o2register/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])


(defn o2register-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (o2register-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((o2register-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn o2register-document-template [doc-name data-set focus fun ]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [o2register-row :id "Id" "number" data-set focus]
       [o2register-row :tehsil "Tehsil" "text" data-set focus]
       [o2register-row :s-no "S.no" "number" data-set focus]
       [o2register-row :date-of-institution "Date of Institution" "date" data-set focus]
       [o2register-row :source-of-receipt "Source Of Receipt" "text" data-set focus]
       [o2register-row :name-of-village "Name Of Village" "text" data-set focus]
       [o2register-row :name-of-the-partys "Name of the Partys" "text" data-set focus]
       [o2register-row :date-of-receipt-from-panchayat "Date of Receipt from Panchayat" "date" data-set focus]
       [o2register-row :date-and-gist-of-final-order "Date and gist of final order" "date" data-set focus]
       [o2register-row :rack-location "Rack Location" "number" data-set focus]
       [o2register-row :starting-year-of-o2-register "Starting year of o2 register" "number" data-set focus]
       [o2register-row :ending-year-of-o2-register "Ending year of 02 Register" "number" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn o2register-save-onclick [data-set focus]
  (if (= nil (o2register-validator @data-set))
    (do
      ;; (swap! o2register-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))



(defn o2register-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [o2register-document-template
       "New Document"
       add-data focus
       #(o2register-save-onclick add-data focus)])))

(defn o2register-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [o2register-document-template
       "Update Document"
       update-data focus
       #(o2register-save-onclick update-data focus)])))



;;------------------------------------------------------------
;;o4 register

(def o4register-dataset (atom [{:id 1
                                :tehsil 101
                                :khata-khatauni-number 123
                                :number-and-date-of-order "12/2/1996"
                                :khasra-number 182
                                :area "warangal"
                                :revenue-rent-of-share-of-plots-transferred 2821
                                :name-and-description-of-persons-removed "A"}
                               ]))


(defn o4register-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "Field is required"]]
                     :tehsil [[v/required :message "Field is required"]]
                     :khata-khatauni-number [[v/required :message "Field is required"]]
                     :number-and-date-of-order [[v/required :message "Field is required"]]
                     :khasra-number [[v/required :message "Field is required"]]
                     :area [[v/required :message "Field is required"]]
                     :revenue-rent-of-share-of-plots-transferred [[v/required :message "Field is required"]]
                     :name-and-description-of-persons-removed [[v/required :message "Field is required"]])))

(defn o4register-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/o4register/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "Tehsil"]
       [:th "khata khatauni Number"]
       [:th "Number and Date of Order"]
       [:th "Khasra Number"]
       [:th "Area"]
       [:th "Revenue Rent of share of plots Transferred"]
       [:th "Name and Description of Persons removed"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :tehsil)]
         [:td (doc :khata-khatauni-number)]
         [:td (doc :number-and-date-of-order)]
         [:td (doc :khasra-number)]
         [:td (doc :area)]
         [:td (doc :revenue-rent-of-share-of-plots-transferred)]
         [:td (doc :name-and-description-of-persons-removed)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/o4register/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])


(defn o4register-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (o4register-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((o4register-validator @data-set) id)))]])
                          [:div])]]]] )))

(defn o4register-document-template [doc-name data-set focus fun]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [o4register-row :id "Id" "number" data-set focus]
       [o4register-row :tehsil "Tehsil" "text" data-set focus]
       [o4register-row :khata-khatauni-number "Khata-khatuni-number" "text" data-set focus ]
       [o4register-row :number-and-date-of-order "Number and Date Of order" "date" data-set focus ]
       [o4register-row :khasra-number "khasra number" "text" data-set focus ]
       [o4register-row :area "Area" "text" data-set focus ]
       [o4register-row :revenue-rent-of-share-of-plots-transferred "revenue rent of share of plots transferred" "text" data-set focus ]
       [o4register-row :name-and-description-of-persons-removed "name and description of persons removed" "text" data-set focus ]
       [:button.btn.btn-primary {:on-click fun} "Save"]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "cancel"]]]]]])

(defn o4register-save-onclick [data-set focus]
  (if (= nil (o4register-validator @data-set))
    (do
      (swap! o4register-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))



(defn o4register-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [o4register-document-template
       "New Document"
       add-data focus
       #(o4register-save-onclick add-data focus)])))

(defn o4register-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [o4register-document-template
       "Update Document"
       update-data focus
       #(o4register-save-onclick update-data focus)])))


;; ----------------------------------
;; o6register form

(def o6register-dataset (atom [{:id 1
                                :tehsil 101
                                :year "05/09/1998"
                                :mehsil-number 173
                                :date-of-order-levy "05/07/2009"
                                :name-of-village "vanaparhi"
                                :name-of-person-whom-recovery-is-made "A"}
                               ]))

(defn o6register-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :tehsil [[v/required :message "field is required"]]
                     :year [[v/required :message "field is required"]]
                     :mehsil-number [[v/required :message "field is required"]]
                     :date-of-order-levy [[v/required :message "field is required"]]
                     :name-of-village [[v/required :message "field is required"]]
                     :name-of-person-whom-recovery-is-made [[v/required :message "field is required"]])))

(defn o6register-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/o6register/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "Tehsil"]
       [:th "Year"]
       [:th "Mehsil Number"]
       [:th "Date of order levy"]
       [:th "Name of Village"]
       [:th "Name of person whom recovery is made"]
       [:th "Update"]
       [:th "Delete"]]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :tehsil)]
         [:td (doc :year)]
         [:td (doc :mehsil-number)]
         [:td (doc :date-of-order-levy)]
         [:td (doc :name-of-village)]
         [:td (doc :name-of-person-whom-recovery-is-made)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/o6register/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])


(defn o6register-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (o6register-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((o6register-validator @data-set) id)))]])
                          [:div])]]]] )))


(defn o6register-document-template [doc-name data-set focus fun]
  [:div
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [o6register-row :id "Id" "number" data-set focus]
       [o6register-row :tehsil "Tehsil" "text" data-set focus]
       [o6register-row :year "Year" "date" data-set focus ]
       [o6register-row :mehsil-number "Mehsil Number" "text" data-set focus ]
       [o6register-row :date-of-order-levy "Date of order levy" "date" data-set focus ]
       [o6register-row :name-of-village "Name of Village" "text" data-set focus]
       [o6register-row :name-of-person-whom-recovery-is-made "Name of Person whom recovery is made" "text" data-set focus ]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/") } "Cancel"]]]]]])


(defn o6register-save-onclick [data-set focus]
  (if (= nil (o6register-validator @data-set))
    (do
      (swap! o6register-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))



(defn o6register-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [o6register-document-template
       "New Document"
       add-data focus
       #(o6register-save-onclick add-data focus)])))

(defn o6register-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [o6register-document-template
       "Update Document"
       update-data focus
       #(o6register-save-onclick update-data focus)])))



;; ---------------------------------------------
;; mutation records

(def mutation-dataset (atom [{:id 1
                              :mutation-number 101
                              :name-of-party "pooram"
                              :date-if-institution "house-sales"
                              :name-of-po "Appa Rao"
                              :date-of-decision 2008
                              :title "A"
                              :khasra-number 123
                              :khata-khatuni-number 542
                              :name-of-village "obulapuram"
                              :sub-division-name "hyd"
                              :name-of-district "ngd"
                              :o2-number 121
                              :o4-number 76
                              :o6-number 38
                              :rack-number 39
                              }]))

(defn mutation-validator [data-set]
  (first (b/validate data-set
                     :id  [[v/required :message "field is required"]]
                     :mutation-number [[v/required :message "field is required"]]
                     :name-of-party [[v/required :message "field is required"]]
                     :date-if-institution [[v/required :message "field is required"]]
                     :name-of-po [[v/required :message "field is required"]]
                     :date-of-decision [[v/required :message "field is required"]]
                     :title [[v/required :message "field is required"]]
                     :khasra-number [[v/required :message "field is required"]]
                     :khata-khatuni-number [[v/required :message "field is required"]]
                     :name-of-village [[v/required :message "field is required"]]
                     :sub-division-name [[v/required :message "field is required"]]
                     :name-of-district [[v/required :message "field is required"]]
                     :o2-number [[v/required :message "field is required"]]
                     :o4-number [[v/required :message "field is required"]]
                     :o6-number [[v/required :message "field is required"]]
                     :rack-number [[v/required :message "field is required"]])))

(defn mutation-table [all-documents]
  [:div
   [:button.btn.btn-primary {:on-click #(accountant/navigate! "/mutation/add")} "add-new"]
   [:div.box-body
    [:table.table.table-striped
     [:thead
      [:tr
       [:th "Mutation Number"]
       [:th "Name of Party"]
       [:th "Date if Institution"]
       [:th "Name of P.O"]
       [:th "Date of Decision"]
       [:th "Title"]
       [:th "Khasra Number"]
       [:th "Khata Khatuni Number"]
       [:th "Name of Village"]
       [:th "Sub Division Name"]
       [:th "Name of District"]
       [:th "02 Number"]
       [:th "04 Number"]
       [:th "06 Number"]
       [:th "Rack Number"]
       [:th "Update"]
       [:th "Delete"]
       ]]
     [:tbody
      (for [doc all-documents]
        ^{:key (doc :id)}
        [:tr
         [:td (doc :mutation-number)]
         [:td (doc :name-of-party)]
         [:td (doc :date-if-institution)]
         [:td (doc :name-of-po)]
         [:td (doc :date-of-decision)]
         [:td (doc :title)]
         [:td (doc :khasra-number)]
         [:td (doc :khata-khatuni-number)]
         [:td (doc :name-of-village)]
         [:td (doc :sub-division-name)]
         [:td (doc :name-of-district)]
         [:td (doc :o2-number)]
         [:td (doc :o4-number)]
         [:td (doc :o6-number)]
         [:td (doc :rack-number)]
         [:td  [:button.btn.btn-success.btn-sm.glyphicon.glyphicon-edit
                {:on-click #(accountant/navigate! "/mutation/update")}]]
         [:td  [:button.btn.btn-danger.btn-sm.glyphicon.glyphicon-remove]]])]]]])

(defn mutation-row [id label ttype data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-element id ttype data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (mutation-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((mutation-validator @data-set) id)))]])
                          [:div])]]]])))


;; (def name-po ["Kumar" "Sai" "Bhaskar" "Rajesh"])

(defn datalist []
  [:datalist {:id "combo"}
   (let [name-po ["Kumar" "Sai" "Bhaskar" "Rajesh"]]
     (for [i name-po]
       ^{:key i}
       [:option {:value i}]))])

(defn input-combo [id data-set placeholder in-focus]
  [:input.form-control {:id id
                        :list "combo"
                        :value (@data-set id)
                        :placeholder placeholder
                        :on-change #(swap! data-set assoc id (-> % .-target .-value))
                        :on-blur  #(reset! in-focus "on")
                        }
   [datalist ]])


(defn mutation-row-combo [id label data-set focus]
  (let [input-focus (atom nil)]
    (fn []
      [:div.form-group
       [:div.col-md-12
        [:div.row
         [:div.col-md-2 [:label label]]
         [:div.col-md-7 [input-combo id data-set label input-focus]]
         [:div.col-md-3 (if (or @input-focus @focus)
                          (if (= nil (mutation-validator @data-set))
                            [:div]
                            [:div {:style  {:color "red"}}
                             [:b (str (first ((mutation-validator @data-set) id)))]])
                          [:div])]]]])))


(defn mutation-document-template [doc-name data-set focus fun]
  [:div.container
   [:div.panel.panel-primary.model-dialog
    [:div.panel-heading
     [:h2 doc-name]]
    [:div.panel-boby
     [:div.container
      [:div.form-group
       [mutation-row :id "Id" "number" data-set focus]
       [mutation-row :mutation-number "Mutation Number" "text" data-set focus ]
       [mutation-row :name-of-party "Name of District" "text" data-set focus ]
       [mutation-row :date-if-institution "Date if institution" "date" data-set focus ]
       ;; [:select#name-of-po [:option {:value "abc"} "abc"] [:option {:value "def"} "def"]]
       [mutation-row-combo :name-of-po "Name of P.O" data-set focus ]
       [mutation-row :date-of-decision "Date of Decision" "date" data-set focus]
       [mutation-row :title "Title" "text" data-set focus]
       [mutation-row :khasra-number "Khasra Number" "text" data-set focus]
       [mutation-row :khata-khatuni-number "Khata Khatuni Number" "text" data-set focus]
       [mutation-row :name-of-village "Name of Village" "text" data-set focus]
       [mutation-row :sub-division-name "Sub Division Name" "text" data-set focus]
       [mutation-row :name-of-district "Name of District" "text" data-set focus]
       [mutation-row :o2-number "O2 Number" "text" data-set focus]
       [mutation-row :o4-number "O4 Number" "text" data-set focus]
       [mutation-row :o6-number "O6 Number" "text" data-set focus]
       [mutation-row :rack-number "Rack Number" "text" data-set focus]
       [:button.btn.btn-primary {:on-click fun} "Save" ]
       [:button.btn.btn-primary {:on-click #(accountant/navigate! "/")} "Cancel"]]]]]])


(defn mutation-save-onclick [data-set focus]
  (if (= nil (mutation-validator @data-set))
    (do
      (swap! mutation-dataset conj @data-set)
      (accountant/navigate! "/"))
    (reset! focus "on")))


(defn mutation-add-record []
  (let [add-data (atom {})
        focus (atom nil)]
    (fn []
      [mutation-document-template
       "New Document"
       add-data
       focus
       #(mutation-save-onclick add-data focus)])))

(defn mutation-upd-record [id dmt]
  (let [update-data (atom {})
        focus (atom nil)]
    (fn []
      [mutation-document-template
       "Update Document"
       update-data
       focus
       #(mutation-save-onclick update-data focus)])))
