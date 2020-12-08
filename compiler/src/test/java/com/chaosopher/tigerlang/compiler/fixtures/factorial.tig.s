.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
# start main
# start 
L5:
movq %rdi, -8(%rbp) # store to offset 1
movq %r15, -16(%rbp) # spill store
movq $1, %rax # integerExpression
cmp %rax, %rsi
je L1
L2:
movq %rsi, %r15 # move(t, t)
movq -8(%rbp), %rdi # mem(boe)
movq $1, %rax # bin(t, i)
sub %rax, %rsi
call L0
movq %rax, %rcx # rax to temp 
movq %r15, %rax # imul l -> rax
imul %rcx # imul rax * r 
L3:
movq -16(%rbp), %r15 # spill load
jmp L4
L1:
movq $1, %rax # move(t, i)
jmp L3
L4:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $0, %rsp
# start main
# start 
L7:
movq $0, %rax # move(t, i)
movq $5, %rsi # integerExpression
movq %rbp, %rdi # move reg arg 0 to temp
call L0
movq %rax, %rdi # move reg arg 0 to temp
call printi
jmp L6
L6:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
