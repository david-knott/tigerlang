.global tigermain
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L7:
movq %r15, -8(%rbp) # spill store
movq $10, %r15 # move(t, i)
L1:
movq $0, %rcx # integerExpression
cmp %rcx, %r15
jg L2
L0:
movq -8(%rbp), %r15 # spill load
jmp L6
L2:
movq $5, %rcx # integerExpression
cmp %rcx, %r15
je L3
L4:
L5:
movq %r15, %rdi # move reg arg 0 to temp
call printi
movq $1, %rcx # bin(t, i)
sub %rcx, %r15
jmp L1
L3:
jmp L0
L8:
jmp L5
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
