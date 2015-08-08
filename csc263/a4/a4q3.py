import itertools

class a4q3:
    """ Construct cartesian product for attacker * defender
        and return expected value of loss of attacker.
        
        Examples:
            Attacker rolls 2 dice and defender rolls 1 die.
            
            >>> battle1 = a4q3(2,1)
            >>> battle1.e_value()
            0.4212962962962963
    """
    def __init__(self, attacker, defender):
        """ (a4q3, int, int) -> NoneType
        
        Construct attacker/defender lists.
        """
        # Initialize lists for attacker and defender
        self.attacker_list = [];
        self.defender_list = [];
        
        # Create cartesian product of [1,2,3,4,5,6] depends on
        # a number of dice, and append it to each list.
        for i in itertools.product([1,2,3,4,5,6], repeat=attacker):
            self.attacker_list.append(list(i))
        for i in itertools.product([1,2,3,4,5,6], repeat=defender):
            self.defender_list.append(list(i))
        
        # Sort each nested list in attacker/defender list.
        for lst in self.attacker_list:
            lst.sort(reverse=True)
        for lst in self.defender_list:
            lst.sort(reverse=True)
        
        # Sort attacker/defender list.
        self.attacker_list.sort(reverse=True)
        self.defender_list.sort(reverse=True)

    def e_value(self):
        """ (a4q3) -> double
        
        Return the expected value of loss of attacker.
        """
        i = 0
        num = 0
        loss = 0
        pr_loss = []
        num_attacker = len(self.attacker_list[0])
        num_defender = len(self.defender_list[0])
        # The itereation terminates when there are no more die in
        # either attacker or defender.
        while i < num_attacker and i < num_defender:
            for attack in self.attacker_list:
                for defend in self.defender_list:
                    num += 1
                    if defend[i] >= attack[i]:
                        loss += 1
            # Append numerator and denominator (loss and num) of each battle.
            pr_loss.append([loss, num])
            i += 1 # Increase count
            num = 0
            loss = 0
            
        e_val_loss = 0
        # Calculate the expected value and return the value.
        for result in pr_loss:
            e_val_loss += result[0] / result[1]
        return e_val_loss
