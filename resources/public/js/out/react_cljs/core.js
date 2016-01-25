// Compiled by ClojureScript 1.7.170 {}
goog.provide('react_cljs.core');
goog.require('cljs.core');
goog.require('goog.dom');
goog.require('reagent.core');
goog.require('secretary.core');
goog.require('bouncer.validators');
goog.require('bouncer.core');
goog.require('goog.history.EventType');
goog.require('goog.History');
goog.require('goog.events');
react_cljs.core.home = (function react_cljs$core$home(){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"div.pageheader","div.pageheader",1940182269),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.Keyword(null,"h2","h2",-372662728),"Hello World"], null)], null);
});
react_cljs.core.render_sample = (function react_cljs$core$render_sample(){
return reagent.core.render_component.call(null,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [react_cljs.core.home], null),document.getElementById("app"));
});
react_cljs.core.render_sample.call(null);

//# sourceMappingURL=core.js.map