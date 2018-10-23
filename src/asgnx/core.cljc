(ns asgnx.core
  (:require [clojure.string :as string]
            [clojure.core.async :as async :refer [go chan <! >!]]
            [asgnx.kvstore :as kvstore
             :refer [put! get! list! remove!]]))


;; Do not edit!
;; A def for the course home page URL.
(def cs4278-brightspace "https://brightspace.vanderbilt.edu/d2l/home/85892")


;; Do not edit!
;; A map specifying the instructor's office hours that is keyed by day of the week.
(def instructor-hours {"tuesday"  {:start    8
                                   :end      10
                                   :location "the chairs outside of the Wondry"}

                       "thursday" {:start    8
                                   :end      10
                                   :location "the chairs outside of the Wondry"}})


;; This is a helper function that you might want to use to implement
;; `cmd` and `args`.
(defn words [msg]
  (if msg
      (string/split msg #" ")
      []))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the first word in a text
;; message.
;;
;; Example: (cmd "foo bar") => "foo"
;;
;; See the cmd-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn cmd [msg] (first (words msg)))





;; Asgn 1.
;;
;; @Todo: Fill in this function to return the list of words following
;; the command in a text message.
;;
;; Example: (args "foo bar baz") => ("bar" "baz")
;;
;; See the args-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn args [msg] (rest (words msg)))


;; Asgn 1.
;;
;; @Todo: Fill in this function to return a map with keys for the
;; :cmd and :args parsed from the msg.
;;
;; Example:
;;
;; (parsed-msg "foo bar baz") => {:cmd "foo" :args ["bar" "baz"]}
;;
;; See the parsed-msg-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn parsed-msg [msg]
  {:cmd (first (string/split msg #" "))
   :args (rest (string/split msg #" "))})




;; Asgn 1.
;;
;; @Todo: Fill in this function to prefix the first of the args
;; in a parsed message with "Welcome " and return the result.
;;
;; Example:
;;
;; (welcome {:cmd "welcome" :args ["foo"]}) => "Welcome foo"
;;
;; See the welcome-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn welcome [pmsg](str "Welcome " (first (get pmsg :args))))

;; Asgn 1.
;;
;; @Todo: Fill in this function to return the CS 4278 home page.
;; Use the `cs4278-brightspace` def to produce the output.
;;
;; See the homepage-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn homepage [_] cs4278-brightspace)


;; Asgn 1.
;;
;; @Todo: Fill in this function to convert from 0-23hr format
;; to AM/PM format.
;;
;; Example: (format-hour 14) => "2pm"
;;
;; See the format-hour-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn format-hour [h]
  (if (or (= h 12) (= h 0))
    (str "12" (if (= h 0) "am" "pm"))
    (if (< h 12)(str h "am") (str (- h 12) "pm"))))


;; Asgn 1.
;;
;; @Todo: This function should take a map in the format of
;; the values in the `instructor-hours` map (e.g. {:start ... :end ... :location ...})
;; and convert it to a string format.
;;
;; Example:
;; (formatted-hours {:start 8 :end 10 :location "the chairs outside of the Wondry"}))
;; "from 8am to 10am in the chairs outside of the Wondry"
;;
;; You should use your format-hour function to implement this.
;;
;; See the formatted-hours-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn formatted-hours [hours]
  (str
   "from "
   (format-hour (get hours :start))
   " to "
   (format-hour (get hours :end)) " in "
   (get hours :location)))


;; Asgn 1.
;;
;; @Todo: This function should lookup and see if the instructor
;; has office hours on the day specified by the first of the `args`
;; in the parsed message. If so, the function should return the
;; `formatted-hours` representation of the office hours. If not,
;; the function should return "there are no office hours on that day".
;; The office hours for the instructor should be obtained from the
;; `instructor-hours` map.
;;
;; You should use your formatted-hours function to implement this.
;;
;; See the office-hours-for-day-test in test/asgnx/core_test.clj for the
;; complete specification.
;;
(defn office-hours [{:keys [args cmd]}]
  (if (or (= (first args) "tuesday")(= (first args) "thursday"))
    (formatted-hours (get instructor-hours (first args)))
    "there are no office hours on that day"))


;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msg that takes
;; a destination for the msg in a parameter called `to`
;; and the message in a parameter called `msg` and returns
;; a map with the keys :to and :msg bound to each parameter.
;; The map should also have the key :action bound to the value
;; :send.
;;
(defn action-send-msg [to msg] {:action :send :to to :msg msg})

;; Asgn 2.
;;
;; @Todo: Create a function called action-send-msgs that takes
;; takes a list of people to receive a message in a `people`
;; parameter and a message to send them in a `msg` parmaeter
;; and returns a list produced by invoking the above `action-send-msg`
;; function on each person in the people list.
;;
;; java-like pseudo code:
;;
;; output = new list
;; for person in people:
;;   output.add( action-send-msg(person, msg) )
;; return output
;;
(defn action-send-msgs [people msg]
  (map #(action-send-msg % msg) people))


;; Create a function called action-insert that takes
;; a list of keys in a `ks` parameter, a value to bind to that
;; key path to in a `v` parameter, and returns a map with
;; the key :ks bound to the `ks` parameter value and the key :v
;; vound to the `v` parameter value.)
;; The map should also have the key :action bound to the value
;; :assoc-in.
(defn action-insert [ks v] {:action :assoc-in :ks ks :v v})


;; Create a function called action-inserts that takes:
;; 1. a key prefix (e.g., [:a :b])
;; 2. a list of suffixes for the key (e.g., [:c :d])
;; 3. a value to bind
;;
;; and calls (action-insert combined-key value) for each possible
;; combined-key that can be produced by appending one of the suffixes
;; to the prefix.
(defn action-inserts [prefix ks v]
  (map #(action-insert % v) (map #(conj prefix %) ks)))

;; Asgn 2.
;;
;; @Todo: Create a function called action-remove that takes
;; a list of keys in a `ks` parameter and returns a map with
;; the key :ks bound to the `ks` parameter value.
;; The map should also have the key :action bound to the value
;; :dissoc-in.
;;
(defn action-remove [ks] {:action :dissoc-in :ks ks})

;registers a line length expert for a location
(defn experts-register [experts topic id info]
  [(action-insert [:expert topic id] {:info info})])


;removes a line length expert for a location
(defn experts-unregister [experts topic id]
  [(action-remove [:expert topic id])])

;message for asking about a location
(defn experts-question-msg [experts question-words]
  (str "Asking " (count experts) " employee(s) about the line length."))


;asks for the length of a line at a location
(defn ask-line-length [experts {:keys [args user-id]}]
  (if (empty? args)
    [[] "You must ask for a location."]
    (if (empty? experts)
      [[] "There are no employees at that location."]
      (let [rest-args (rest args)
            msg (string/join " " rest-args)]

        [(concat (action-inserts [:conversations] experts {:last-question (str "What is the line length at" (first args)) :asker user-id})
                 (action-send-msgs experts (str "What is the line length at: " (first args))))
         (experts-question-msg experts rest-args)]))))

;asks for the menu at a location
(defn ask-menu [menu {:keys [args user-id]}]
  (if (empty? menu)
    [[] "That location does not have a menu."]
    [[] (str "Menu at " (first args) ": " menu)]))

;sets the menu for a location
(defn set-menu [location {:keys [args user-id]}]
   [[(action-insert [:menu (first args)] (string/join " " (rest args)))]
    (str "You have registered a menu for the location: " (first args) ".")])

;asks for the hours at a location
(defn ask-hrs [hours {:keys [args user-id]}]
  (if (empty? hours)
    [[] "That location does not have any hours."]
    [[] (str "Hours at " (first args) ": " hours)]))

;sets the hours for a location
(defn set-hrs [location {:keys [args user-id]}]
   [[(action-insert [:hours (first args)] (string/join " " (rest args)))]
    (str "You have registered hours for the location: " (first args) ".")])

;answers the line length question asked of from a line length expert
(defn answer-question [conversation {:keys [args user-id]}]
  (if (empty? (rest args))
    [[] "You did not provide an answer."]
    (if (nil? conversation)
      [[] "You haven't been asked a question."]
      [[(action-send-msg (:asker conversation) (string/join " " args))] "Your answer was sent."])))


;adds a line length expert for a location
(defn add-line-expert [experts {:keys [args user-id]}]
  [(experts-register experts (first args) user-id (rest args))
   (str user-id " is now an expert on " (first args) ".")])

;; Don't edit!
(defn stateless [f]
  (fn [_ & args]
    [[] (apply f args)]))


; routes for commands. works for both lower and uppercased first letter. rest must be lower.
(def routes {;Lowercase
             "default"  (stateless (fn [& args] "Unknown command."))
             "helpme"     (stateless (fn [& args] "Welcome to the Vanderbilt Dining Information Service (VDIS)!\nVDIS helps get you information about the dining halls around Vanderbilt.\n\nCommands:\nline <location>\nmenu <location>\nhours <location>\n\nIf you are an employee, contact admin for more commands."))
             "expert"   add-line-expert
             "line"     ask-line-length
             "answer"   answer-question
             "menu"     ask-menu
             "set-menu" set-menu
             "hours"    ask-hrs
             "set-hours" set-hrs
             ;Caps
             "Helpme"     (stateless (fn [& args] "Welcome to the Vanderbilt Dining Information Service (VDIS)!\nVDIS helps get you information about the dining halls around Vanderbilt.\n\nCommands:\nline <location>\nmenu <location>\nhours <location>\n\nIf you are an employee, contact admin for more commands."))
             "Expert"   add-line-expert
             "Line"     ask-line-length
             "Answer"   answer-question
             "Menu"     ask-menu
             "Set-menu" set-menu
             "Hours"    ask-hrs
             "Set-hours" set-hrs})

;gets the expert on a topic
(defn experts-on-topic-query [state-mgr pmsg]
  (let [[topic]  (:args pmsg)]
    (list! state-mgr [:expert topic])))

;gets the conversation for a user
(defn conversations-for-user-query [state-mgr pmsg]
  (let [user-id (:user-id pmsg)]
    (get! state-mgr [:conversations user-id])))

;gets the menu for a location
(defn menu-for-location-query [state-mgr pmsg]
  (let [[location]  (:args pmsg)]
    (get! state-mgr [:menu location])))

;gets the hours for a location
(defn hours-for-location-query [state-mgr pmsg]
  (let [[location]  (:args pmsg)]
    (get! state-mgr [:hours location])))


;query mappings for different commands. works for both lower and uppercased first letter. rest must be lower.
(def queries
  {"expert" experts-on-topic-query ;Lowercase
   "line"   experts-on-topic-query
   "answer" conversations-for-user-query
   "menu"   menu-for-location-query
   "set-menu" menu-for-location-query
   "hours"  hours-for-location-query
   "set-hours" hours-for-location-query
   "Expert" experts-on-topic-query ;Caps
   "Line"   experts-on-topic-query
   "Answer" conversations-for-user-query
   "Menu"   menu-for-location-query
   "Set-menu" menu-for-location-query
   "Hours"  hours-for-location-query
   "Set-hours" hours-for-location-query})




;; Don't edit!
(defn read-state [state-mgr pmsg]
  (go
    (if-let [qfn (get queries (:cmd pmsg))]
      (<! (qfn state-mgr pmsg))
      {})))


;Creates a router
(defn create-router [routes] (fn [pmsg] (let [routeCmd (get routes (get pmsg :cmd))]
                                          (if (nil? routeCmd) (get routes "default") routeCmd))))

;; Don't edit!
(defn output [o]
  (second o))


;; Don't edit!
(defn actions [o]
  (first o))


;; Don't edit!
(defn invoke [{:keys [effect-handlers] :as system} e]
  (go
    (println "    Invoke:" e)
    (if-let [action (get effect-handlers (:action e))]
      (do
        (println "    Invoking:" action "with" e)
        (<! (action system e))))))


;; Don't edit!
(defn process-actions [system actions]
  (go
    (println "  Processing actions:" actions)
    (let [results (atom [])]
      (doseq [action actions]
        (let [result (<! (invoke system action))]
          (swap! results conj result)))
      @results)))


;; Don't edit!
(defn handle-message
  "
    This function orchestrates the processing of incoming messages
    and glues all of the pieces of the processing pipeline together.

    The basic flow to handle a message is as follows:

    1. Create the router that will be used later to find the
       function to handle the message
    2. Parse the message
    3. Load any saved state that is going to be needed to process
       the message (e.g., querying the list of experts, etc.)
    4. Find the function that can handle the message
    5. Call the handler function with the state from #3 and
       the message
    6. Run the different actions that the handler returned...these actions
       will be bound to different implementations depending on the environemnt
       (e.g., in test, the actions aren't going to send real text messages)
    7. Return the string response to the message

  "
  [{:keys [state-mgr] :as system} src msg]
  (go
    (println "=========================================")
    (println "  Processing:\"" msg "\" from" src)
    (let [rtr    (create-router routes)
          _      (println "  Router:" rtr)
          pmsg   (assoc (parsed-msg msg) :user-id src)
          _      (println "  Parsed msg:" pmsg)
          state  (<! (read-state state-mgr pmsg))
          _      (println "  Read state:" state)
          hdlr   (rtr pmsg)
          _      (println "  Hdlr:" hdlr)
          [as o] (hdlr state pmsg)
          _      (println "  Hdlr result:" [as o])
          arslt  (<! (process-actions system as))
          _      (println "  Action results:" arslt)]
      (println "=========================================")
      o)))
