(ns rover.core-test
  (:require [clojure.test :refer :all]
            [rover.core :refer :all]))

(defn- calculate-bearing [bearing command]
  (get {"R" :east} command :north)
  )

(defn- calculate-displacement [bearing command]
    {:delta-x 0 :delta-y (get {"F" 1 "B" -1} command 0)}
  )

(defn- do-command [position command]
  (let [{x :x y :y bearing :bearing} position]
    (let [{delta-x :delta-x delta-y :delta-y} (calculate-displacement bearing command)]
      {:x x :y (+ y delta-y) :bearing (calculate-bearing bearing command)}
      )
    )
  )

(defn move-rover [position commands]
  (reduce do-command position (map str commands))
  )

(deftest mars-rover-movements
  (testing "The rover stays in the same position if it gets no commands"
    (is (= {:x 0 :y 0 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} ""))))
  (testing "The rover moves forward"
    (is (= {:x 0 :y 1 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "F"))))
  (testing "The rover moves backwards"
    (is (= {:x 0 :y -1 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "B"))))
  (testing "The rover moves forward twice"
    (is (= {:x 0 :y 2 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "FF"))))
  (testing "The rover rotates right"
    (is (= {:x 0 :y 0 :bearing :east} (move-rover {:x 0 :y 0 :bearing :north} "R")))))