(ns rover.core-test
  (:require [clojure.test :refer :all]
            [rover.core :refer :all]))

(defn- do-command [position command]
  (get {"F" {:x 0 :y 1 :bearing :north}} command position)
  )

(defn move-rover [position commands]
  (reduce do-command position (map str commands))
  )

(deftest mars-rover-movements
  (testing "The rover stays in the same position if it gets no commands"
    (is (= {:x 0 :y 0 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} ""))))
  (testing "The rover moves forward"
    (is (= {:x 0 :y 1 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "F")))))