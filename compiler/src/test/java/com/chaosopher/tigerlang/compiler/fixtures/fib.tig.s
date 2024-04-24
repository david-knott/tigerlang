.global tigermain
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $24, %rsp
# start main
# start 
L5:
movq %rdi, -8(%rbp) # store to offset 1
movq %rsi, -24(%rbp) # spill store
movq %r14, -16(%rbp) # spill store
movq $2, %rax # integerExpression
movq -24(%rbp), %rsi # spill load
cmp %rax, %rsi
jl L1
L2:
movq -8(%rbp), %rdi # mem(boe)
movq $1, %rax # bin(t, i)
movq -24(%rbp), %rsi # spill load
sub %rax, %rsi
call L0
movq %rax, %r14 # move(t, t)
movq -8(%rbp), %rdi # mem(boe)
movq $2, %rax # bin(t, i)
movq -24(%rbp), %rsi # spill load
sub %rax, %rsi
call L0
movq %rax, %rcx # rax to temp 
movq %r14, %rax # add lexp -> r
add %rcx, %rax
L3:
movq -16(%rbp), %r14 # spill load
jmp L4
L1:
movq -24(%rbp), %rsi # spill load
movq %rsi, %rax # move(t, t)
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
movq $40, %rsi # integerExpression
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
