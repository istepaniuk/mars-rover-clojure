(ns rover.core-test
  (:require [clojure.test :refer :all]
            [rover.core :refer :all]))

(deftest the-mars-rover
  (testing "stays in the same position if it gets no commands"
    (is (= {:x 0 :y 0 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "" []))))
  (testing "moves forward"
    (is (= {:x 0 :y 1 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "F" []))))
  (testing "moves backwards"
    (is (= {:x 0 :y 1 :bearing :north} (move-rover {:x 0 :y 2 :bearing :north} "B" []))))
  (testing "moves forward twice"
    (is (= {:x 0 :y 2 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "FF" []))))
  (testing "rotates right"
    (is (= {:x 0 :y 0 :bearing :east} (move-rover {:x 0 :y 0 :bearing :north} "R" []))))
  (testing "rotates right twice"
    (is (= {:x 0 :y 0 :bearing :south} (move-rover {:x 0 :y 0 :bearing :north} "RR" []))))
  (testing "rotates left"
    (is (= {:x 0 :y 0 :bearing :west} (move-rover {:x 0 :y 0 :bearing :north} "L"[]))))
  (testing "moves forward while heading east"
    (is (= {:x 1 :y 0 :bearing :east} (move-rover {:x 0 :y 0 :bearing :east} "F" []))))
  (testing "The position wraps arround 100 because the planet is round"
    (is (= {:x 1 :y 99 :bearing :east} (move-rover {:x 99 :y 1 :bearing :south} "FFLFF" []))))
  (testing "stops moving when it encounters an obstacle"
    (is (= {:x 0 :y 2 :bearing :north} (move-rover {:x 0 :y 0 :bearing :north} "FFFF" [{:x 0 :y 3}])))))