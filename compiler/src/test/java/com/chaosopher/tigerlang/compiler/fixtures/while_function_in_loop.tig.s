.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L4:
movq %r15, -8(%rbp) # spill store
movq $0, %r15 # move(t, i)
L1:
movq $12, %rcx # integerExpression
cmp %rcx, %r15
jle L2
L0:
movq -8(%rbp), %r15 # spill load
jmp L3
L2:
movq %r15, %rdi # move reg arg 0 to temp
call printi
movq $1, %rcx # bin(t, i)
add %rcx, %r15
jmp L1
L3:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
