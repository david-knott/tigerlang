.global tigermain
.data
L9:
	.long  0x1
	.ascii "x\0"
.data
L10:
	.long  0x1
	.ascii "y\0"
.data
L14:
	.long  0x2
	.ascii "\n\0"
.data
L15:
	.long  0x2
	.ascii "\n\0"
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $40, %rsp
# start main
# start 
L17:
movq %rdi, -8(%rbp) # store to offset 1
movq %r12, -40(%rbp) # spill store
movq %r13, -32(%rbp) # spill store
movq %r14, -24(%rbp) # spill store
movq %r15, -16(%rbp) # spill store
movq $0, %r12 # move(t, i)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %r13 # mem(boe)
movq $1, %rax # bin(t, i)
sub %rax, %r13
L1:
cmp %r13, %r12
jle L2
L4:
movq $L15, %rdi # default name
call print
movq -40(%rbp), %r12 # spill load
movq -32(%rbp), %r13 # spill load
movq -24(%rbp), %r14 # spill load
movq -16(%rbp), %r15 # spill load
jmp L16
L2:
movq $0, %r15 # move(t, i)
movq -8(%rbp), %rax # mem(boe)
movq -8(%rax), %r14 # mem(boe)
movq $1, %rax # bin(t, i)
sub %rax, %r14
L5:
cmp %r14, %r15
jle L6
L8:
movq $L14, %rdi # default name
call print
cmp %r13, %r12
je L4
L3:
movq $1, %rax # bin(t, i)
add %rax, %r12
jmp L1
L6:
cmp %r15, %r12
jg L11
L12:
movq $L10, %rdi # default name
L13:
call print
cmp %r14, %r15
je L8
L7:
movq $1, %rax # bin(t, i)
add %rax, %r15
jmp L5
L11:
movq $L9, %rdi # default name
jmp L13
L16:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.text
tigermain:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L19:
movq $8, -8(%rbp) # store to offset 2
movq %rbp, %rdi # move reg arg 0 to temp
call L0
jmp L18
L18:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
