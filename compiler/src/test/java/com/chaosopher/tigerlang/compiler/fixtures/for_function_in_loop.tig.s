.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L5:
movq %r14, -16(%rbp) # spill store
movq %r15, -8(%rbp) # spill store
movq $2, %r14 # move(t, i)
movq $12, %r15 # move(t, i)
L0:
cmp %r15, %r14
jle L1
L3:
movq -16(%rbp), %r14 # spill load
movq -8(%rbp), %r15 # spill load
jmp L4
L1:
movq %r14, %rdi # move reg arg 0 to temp
call printi
cmp %r15, %r14
je L3
L2:
movq $1, %rcx # bin(t, i)
add %rcx, %r14
jmp L0
L4:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
