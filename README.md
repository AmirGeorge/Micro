Micro
=====

Microprocessors project - Semester 7

Covered material for deliverable 2: Lecture 13

Q&A:
====

1) For an m-way superscalar architecture: provided that there are no hazards, can the processor pass m instructions to the execute stage 
(or any stage other than the issue stage) at the same time or does m-way mean it can only ISSUE m instructions at the same time ?
- it can pass

2) does superscalar mean we will have to wait for m empty places in the ROB before we issue another m instructions ? I mean can't we issue 1 instruction if 
there is only one available ROB entry at that time ?
- no we don't have if only one ROB place is empty we can issue one instruction at that time.

3) What is the function of the instruction buffer (queue) whose size is taken as input from the user ?
- fetched instructions are put in the instruction buffer queue until they are issued.(We can fetch maximum of m instructions at a time for an m-way 
superscalar processor)

4) Concerning the caches already implemented in deliverable 1; I understand that accessing the data caches will happen in the execute stage of load and store 
instructions, but when will the instruction caches be accessed (at the fetch stage for example ?) ?
- we can neglect instruction caches completely and assume fetch happens in only one cycle.

5) (may be related to 4) what exactly happens in the fetch stage ?
- fetching the instruction from the instruction buffer, happens in only one cycle

6) What exactly are the different functional units available ? i.e. ADD,MUL,LD,.. ?
- Load / Store (no given latency for them, their latency is that of the data caches)
   Mul (and divide)
   Add (math operations + Jump + BEQ)
   Logic (logical operations)

7) What is the meaning of the assumption about 1 to 1 mapping between functional units and reservation stations ? I can't understand how it will help me since 
the user will be asked to enter the number of reservation stations for each class of instructions.
- neglect it, anyway we will only have a data structure for reservation stations, we won't represent functional units by anything.

8) What is the meaning of the 7th assumption: "Assume that the branch target address is always predicted correctly when needed" ? I understand that unconditional 
branches will always be predicted correctly. And that conditional branches will be predicted according to the offset sign and if we find in the execute stage 
that the prediction was wrong we flush the ROB, am I correct ? if that is the case when do I need to use the assumption ?
- We calculate the branch target address during fetch, and branch prediction happens in fetch stage(caclculation of address will always be true and 
prediction will be according to offset as described in the pdf)

9) What happens exactly before flush ?
- I discover in the execute stage of my branch instruction that my branch prediction was incorrect so I I flush the ROB entries from this branch instruction 
until just before the head.

10) What should happen with the data caches ?
- For the write and commit instructions, they will take 1 cycle in all instructions except for the store instruction, which spends the latency it takes in the memory hierarchy (Data Cache) during the write stage.
For the load instructions you take into account the data cache during the execute stage normally.