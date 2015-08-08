import Data.List
import Control.Monad

{- Given attacker's dice roll (a) and defender's dice roll (d) (both before sorting),
   this computes how many armies the attacker loses.

   Example: a = [4,1,6], d = [2,6]

   sort a in descending order: sort_dsc a = [6,4,1]
   sort d in descending order: sort_dsc d = [6,2]

   compare them term-wise and determine attacker's loss term-wise:
     zipWith step [6,4,1] [6,2] = [step 6 6, step 4 2] = [1, 0]

   (In general, zipWith f [x1, x2, ...] [y1, y2, ...] = [f x1 y1, f x2 y2, ...]
    and truncates the longer list.)

   Lastly, sum [1, 0] = 1+0 for the total.
-}
attacker_loss a d = sum (zipWith step (sort_dsc a) (sort_dsc d))
  where
    step ai di = if ai > di then 0 else 1
    sort_dsc s = sortBy (flip compare) s

{- Given that the attacker rolls na dice and the defender rolls nd dice:
   go through all possible dice rolls and sum their attacker's losses,
   multiply by each roll's probability 1/6^(na+nd).
   (It's the same probability for each dice roll, so I factor out the division.)

   replicateM works this way, e.g.,
   replicateM 2 [1..6] = [ [1,1], ..., [1,6],
                           [2,1], ..., [2,6],
                           ...
                           [6,1], ..., [6,6] ]
   i.e., all ways of rolling 2 dice.

   "[ expr | var1 <- list1, var2 <- list2 ]" is list comprehension syntax.
   There is something equivalent in Python.

   "fromIntegral" converts integers to other number types (especially fractions).
   Haskell does not have implicit number conversions.
-}
expected_attacker_loss na nd =
  fromIntegral (sum [ attacker_loss a d |
                      a <- replicateM na [1..6],
                      d <- replicateM nd [1..6] ])
  /
  (6 ^ (na + nd))

main = sequence_ [ print_answer na nd | na <- [3,2,1], nd <- [2,1] ]
  where
    print_answer na nd =
      putStrLn ("attacker " ++ show na ++ " dice, " ++
                "defender " ++ show nd ++ " dice: " ++
                show (expected_attacker_loss na nd))
