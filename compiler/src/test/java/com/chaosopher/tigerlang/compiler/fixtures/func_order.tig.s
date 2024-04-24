.global tigermain
.data
L2:
	.long  0x3
	.ascii "one\0"
.text
L0:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L5:
movq %rdi, -8(%rbp) # store to offset 1
movq $L2, %rdi # default name
call print
movq -8(%rbp), %rdi # mem(boe)
call L1
jmp L4
L4:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
.data
L3:
	.long  0x3
	.ascii "two\0"
.text
L1:
pushq %rbp
movq %rsp, %rbp
subq $8, %rsp
# start main
# start 
L7:
movq %rdi, -8(%rbp) # store to offset 1
movq $L3, %rdi # default name
call print
jmp L6
L6:
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
L9:
movq %rbp, %rdi # move reg arg 0 to temp
call L0
jmp L8
L8:
# sink 
# end main
movq %rbp, %rsp
popq %rbp
retq
