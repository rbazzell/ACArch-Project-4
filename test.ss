      --A program to find the sum of a list of numbers
      -- The program uses a subroutine to add 2 numbers, as a demo
      -- It also sets up a stack frame, although not needed for this program
      -- 4000 = # of nums to sum
      -- 4004  = location for sum to be put
      -- 4008 = beginning of array of nums
      -- 
      -- R20, R21 - parameter passing regs
      -- R30 = SP
      -- R31 = Ret Addr Reg
      -- R3 = size of array, in bytes
      -- R4 = Address of beginning of array (4008)
      -- R5 = first address past array, for loop termination
      -- R6 = current address being worked on (loop i variable)
      -- R7 = sum
      -- R8 = current array data value
      -- 
0:  ADDI R1, R0, 4000
4:  LW R2, 0(R1)
8:  ADDI R3, R0, 3
12:  ADD R2, R2, R2
16:  MUL R4, R2, R3
20:  ADD R2, R2, R2
24:  SW R2, 4(R1)
28:  HALT
