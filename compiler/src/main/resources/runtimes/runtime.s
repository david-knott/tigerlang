	.file	"runtime.c"
	.text
.Ltext0:
	.file 0 "/usr/src/app/compiler/src/main/resources/runtimes" "runtime.c"
	.globl	initArray
	.type	initArray, @function
initArray:
.LFB6:
	.file 1 "runtime.c"
	.loc 1 5 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movl	%edi, -20(%rbp)
	movl	%esi, -24(%rbp)
	.loc 1 7 21
	movl	-20(%rbp), %eax
	cltq
	salq	$3, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 8 10
	movl	$0, -12(%rbp)
	.loc 1 8 3
	jmp	.L2
.L3:
	.loc 1 9 6 discriminator 3
	movl	-12(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	movq	-8(%rbp), %rax
	addq	%rax, %rdx
	.loc 1 9 10 discriminator 3
	movl	-24(%rbp), %eax
	cltq
	movq	%rax, (%rdx)
	.loc 1 8 26 discriminator 3
	addl	$1, -12(%rbp)
.L2:
	.loc 1 8 17 discriminator 1
	movl	-12(%rbp), %eax
	cmpl	-20(%rbp), %eax
	jl	.L3
	.loc 1 10 10
	movq	-8(%rbp), %rax
	.loc 1 11 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE6:
	.size	initArray, .-initArray
	.globl	initArrayBoundsChecked
	.type	initArrayBoundsChecked, @function
initArrayBoundsChecked:
.LFB7:
	.loc 1 14 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movl	%edi, -20(%rbp)
	movl	%esi, -24(%rbp)
	.loc 1 16 21
	movl	-20(%rbp), %eax
	cltq
	salq	$3, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 17 15
	movl	-20(%rbp), %eax
	subl	$1, %eax
	movslq	%eax, %rdx
	.loc 1 17 8
	movq	-8(%rbp), %rax
	movq	%rdx, (%rax)
	.loc 1 18 10
	movl	$1, -12(%rbp)
	.loc 1 18 3
	jmp	.L6
.L7:
	.loc 1 19 6 discriminator 3
	movl	-12(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	movq	-8(%rbp), %rax
	addq	%rax, %rdx
	.loc 1 19 10 discriminator 3
	movl	-24(%rbp), %eax
	cltq
	movq	%rax, (%rdx)
	.loc 1 18 26 discriminator 3
	addl	$1, -12(%rbp)
.L6:
	.loc 1 18 17 discriminator 1
	movl	-12(%rbp), %eax
	cmpl	-20(%rbp), %eax
	jl	.L7
	.loc 1 20 10
	movq	-8(%rbp), %rax
	.loc 1 21 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE7:
	.size	initArrayBoundsChecked, .-initArrayBoundsChecked
	.globl	initRecord
	.type	initRecord, @function
initRecord:
.LFB8:
	.loc 1 25 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$48, %rsp
	movl	%edi, -36(%rbp)
	.loc 1 28 19
	movl	-36(%rbp), %eax
	cltq
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 28 5
	movq	-8(%rbp), %rax
	movq	%rax, -16(%rbp)
	.loc 1 29 10
	movl	$0, -20(%rbp)
	.loc 1 29 3
	jmp	.L10
.L11:
	.loc 1 30 7 discriminator 3
	movq	-16(%rbp), %rax
	leaq	8(%rax), %rdx
	movq	%rdx, -16(%rbp)
	.loc 1 30 10 discriminator 3
	movq	$0, (%rax)
	.loc 1 29 27 discriminator 3
	movl	-20(%rbp), %eax
	addl	$8, %eax
	movl	%eax, -20(%rbp)
.L10:
	.loc 1 29 17 discriminator 1
	movl	-20(%rbp), %eax
	cmpl	-36(%rbp), %eax
	jl	.L11
	.loc 1 31 10
	movq	-8(%rbp), %rax
	.loc 1 32 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE8:
	.size	initRecord, .-initRecord
	.globl	streq
	.type	streq, @function
streq:
.LFB9:
	.loc 1 41 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	.loc 1 43 6
	movq	-24(%rbp), %rax
	cmpq	-32(%rbp), %rax
	jne	.L14
	.loc 1 44 12
	movl	$1, %eax
	jmp	.L15
.L14:
	.loc 1 45 8
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	.loc 1 45 21
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 45 6
	cmpl	%eax, %edx
	je	.L16
	.loc 1 46 12
	movl	$0, %eax
	jmp	.L15
.L16:
	.loc 1 47 10
	movl	$0, -4(%rbp)
	.loc 1 47 3
	jmp	.L17
.L19:
	.loc 1 48 17
	movq	-24(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	.loc 1 48 32
	movq	-32(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %eax
	.loc 1 48 8
	cmpb	%al, %cl
	je	.L18
	.loc 1 49 14
	movl	$0, %eax
	jmp	.L15
.L18:
	.loc 1 47 31 discriminator 2
	addl	$1, -4(%rbp)
.L17:
	.loc 1 47 20 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 47 17 discriminator 1
	cmpl	%eax, -4(%rbp)
	jl	.L19
	.loc 1 50 10
	movl	$1, %eax
.L15:
	.loc 1 51 1
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE9:
	.size	streq, .-streq
	.globl	stringEqual
	.type	stringEqual, @function
stringEqual:
.LFB10:
	.loc 1 54 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	.loc 1 56 6
	movq	-24(%rbp), %rax
	cmpq	-32(%rbp), %rax
	jne	.L21
	.loc 1 57 12
	movl	$1, %eax
	jmp	.L22
.L21:
	.loc 1 58 8
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	.loc 1 58 21
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 58 6
	cmpl	%eax, %edx
	je	.L23
	.loc 1 59 12
	movl	$0, %eax
	jmp	.L22
.L23:
	.loc 1 60 10
	movl	$0, -4(%rbp)
	.loc 1 60 3
	jmp	.L24
.L26:
	.loc 1 61 17
	movq	-24(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	.loc 1 61 32
	movq	-32(%rbp), %rdx
	movl	-4(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %eax
	.loc 1 61 8
	cmpb	%al, %cl
	je	.L25
	.loc 1 62 14
	movl	$0, %eax
	jmp	.L22
.L25:
	.loc 1 60 31 discriminator 2
	addl	$1, -4(%rbp)
.L24:
	.loc 1 60 20 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 60 17 discriminator 1
	cmpl	%eax, -4(%rbp)
	jl	.L26
	.loc 1 63 10
	movl	$1, %eax
.L22:
	.loc 1 64 1
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE10:
	.size	stringEqual, .-stringEqual
	.globl	print
	.type	print, @function
print:
.LFB11:
	.loc 1 67 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movq	%rdi, -24(%rbp)
	.loc 1 69 18
	movq	-24(%rbp), %rax
	addq	$4, %rax
	movq	%rax, -8(%rbp)
	.loc 1 70 10
	movl	$0, -12(%rbp)
	.loc 1 70 3
	jmp	.L28
.L29:
	.loc 1 71 13 discriminator 3
	movq	-8(%rbp), %rax
	movzbl	(%rax), %eax
	.loc 1 71 5 discriminator 3
	movzbl	%al, %eax
	movl	%eax, %edi
	call	putchar@PLT
	.loc 1 70 31 discriminator 3
	addl	$1, -12(%rbp)
	.loc 1 70 36 discriminator 3
	addq	$1, -8(%rbp)
.L28:
	.loc 1 70 20 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 70 17 discriminator 1
	cmpl	%eax, -12(%rbp)
	jl	.L29
	.loc 1 72 1
	nop
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE11:
	.size	print, .-print
	.section	.rodata
.LC0:
	.string	"%d"
	.text
	.globl	printi
	.type	printi, @function
printi:
.LFB12:
	.loc 1 75 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movl	%edi, -4(%rbp)
	.loc 1 76 3
	movl	-4(%rbp), %eax
	movl	%eax, %esi
	leaq	.LC0(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 77 1
	nop
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE12:
	.size	printi, .-printi
	.globl	rttest
	.type	rttest, @function
rttest:
.LFB13:
	.loc 1 79 15
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	.loc 1 80 2
	movl	$116, %edi
	call	putchar@PLT
	.loc 1 81 2
	movl	$101, %edi
	call	putchar@PLT
	.loc 1 82 2
	movl	$115, %edi
	call	putchar@PLT
	.loc 1 83 2
	movl	$116, %edi
	call	putchar@PLT
	.loc 1 84 3
	movq	stdout(%rip), %rax
	movq	%rax, %rdi
	call	fflush@PLT
	.loc 1 85 1
	nop
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE13:
	.size	rttest, .-rttest
	.globl	flush
	.type	flush, @function
flush:
.LFB14:
	.loc 1 88 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	.loc 1 89 3
	movq	stdout(%rip), %rax
	movq	%rax, %rdi
	call	fflush@PLT
	.loc 1 90 1
	nop
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE14:
	.size	flush, .-flush
	.globl	consts
	.bss
	.align 32
	.type	consts, @object
	.size	consts, 2048
consts:
	.zero	2048
	.globl	empty
	.align 8
	.type	empty, @object
	.size	empty, 8
empty:
	.zero	8
	.text
	.globl	main
	.type	main, @function
main:
.LFB15:
	.loc 1 96 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	.loc 1 98 10
	movl	$0, -4(%rbp)
	.loc 1 98 3
	jmp	.L34
.L35:
	.loc 1 100 22 discriminator 3
	movl	-4(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	movl	$1, (%rdx,%rax)
	.loc 1 101 24 discriminator 3
	movl	-4(%rbp), %eax
	movl	%eax, %ecx
	movl	-4(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	leaq	4+consts(%rip), %rax
	movb	%cl, (%rdx,%rax)
	.loc 1 98 25 discriminator 3
	addl	$1, -4(%rbp)
.L34:
	.loc 1 98 17 discriminator 1
	cmpl	$255, -4(%rbp)
	jle	.L35
	.loc 1 103 10
	movl	$0, %edi
	movl	$0, %eax
	call	tigermain@PLT
	.loc 1 104 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE15:
	.size	main, .-main
	.globl	ord
	.type	ord, @function
ord:
.LFB16:
	.loc 1 107 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -8(%rbp)
	.loc 1 108 8
	movq	-8(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 108 6
	testl	%eax, %eax
	jne	.L38
	.loc 1 109 12
	movl	$-1, %eax
	jmp	.L39
.L38:
	.loc 1 111 20
	movq	-8(%rbp), %rax
	movzbl	4(%rax), %eax
	movzbl	%al, %eax
.L39:
	.loc 1 112 1
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE16:
	.size	ord, .-ord
	.section	.rodata
.LC1:
	.string	"chr(%d) out of range\n"
	.text
	.globl	chr
	.type	chr, @function
chr:
.LFB17:
	.loc 1 115 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	movq	%rdi, -8(%rbp)
	.loc 1 116 6
	cmpq	$0, -8(%rbp)
	js	.L41
	.loc 1 116 13 discriminator 1
	cmpq	$255, -8(%rbp)
	jle	.L42
.L41:
	.loc 1 118 5
	movq	-8(%rbp), %rax
	movq	%rax, %rsi
	leaq	.LC1(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 119 5
	movl	$1, %edi
	call	exit@PLT
.L42:
	.loc 1 121 17
	movq	-8(%rbp), %rax
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	addq	%rdx, %rax
	.loc 1 122 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE17:
	.size	chr, .-chr
	.globl	size
	.type	size, @function
size:
.LFB18:
	.loc 1 125 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movq	%rdi, -8(%rbp)
	.loc 1 126 11
	movq	-8(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 127 1
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE18:
	.size	size, .-size
	.section	.rodata
	.align 8
.LC2:
	.string	"substring([%d],%d,%d) out of range\n"
	.text
	.globl	substring
	.type	substring, @function
substring:
.LFB19:
	.loc 1 130 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movq	%rdi, -24(%rbp)
	movl	%esi, -28(%rbp)
	movl	%edx, -32(%rbp)
	.loc 1 131 6
	cmpl	$0, -28(%rbp)
	js	.L47
	.loc 1 131 26 discriminator 1
	movl	-28(%rbp), %edx
	movl	-32(%rbp), %eax
	addl	%eax, %edx
	.loc 1 131 33 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 131 17 discriminator 1
	cmpl	%eax, %edx
	jle	.L48
.L47:
	.loc 1 133 5
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	movl	-32(%rbp), %ecx
	movl	-28(%rbp), %edx
	movl	%eax, %esi
	leaq	.LC2(%rip), %rax
	movq	%rax, %rdi
	movl	$0, %eax
	call	printf@PLT
	.loc 1 134 5
	movl	$1, %edi
	call	exit@PLT
.L48:
	.loc 1 136 6
	cmpl	$1, -32(%rbp)
	jne	.L49
	.loc 1 137 29
	movq	-24(%rbp), %rdx
	movl	-28(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %eax
	movzbl	%al, %eax
	.loc 1 137 19
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	addq	%rdx, %rax
	jmp	.L50
.L49:
.LBB2:
	.loc 1 139 41
	movl	-32(%rbp), %eax
	cltq
	addq	$4, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 141 15
	movq	-8(%rbp), %rax
	movl	-32(%rbp), %edx
	movl	%edx, (%rax)
	.loc 1 142 12
	movl	$0, -12(%rbp)
	.loc 1 142 5
	jmp	.L51
.L52:
	.loc 1 143 36 discriminator 3
	movl	-28(%rbp), %edx
	movl	-12(%rbp), %eax
	addl	%edx, %eax
	.loc 1 143 29 discriminator 3
	movq	-24(%rbp), %rdx
	cltq
	movzbl	4(%rdx,%rax), %ecx
	.loc 1 143 19 discriminator 3
	movq	-8(%rbp), %rdx
	movl	-12(%rbp), %eax
	cltq
	movb	%cl, 4(%rdx,%rax)
	.loc 1 142 25 discriminator 3
	addl	$1, -12(%rbp)
.L51:
	.loc 1 142 19 discriminator 1
	movl	-12(%rbp), %eax
	cmpl	-32(%rbp), %eax
	jl	.L52
	.loc 1 144 12
	movq	-8(%rbp), %rax
.L50:
.LBE2:
	.loc 1 146 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE19:
	.size	substring, .-substring
	.globl	concat
	.type	concat, @function
concat:
.LFB20:
	.loc 1 149 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$32, %rsp
	movq	%rdi, -24(%rbp)
	movq	%rsi, -32(%rbp)
	.loc 1 150 8
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 150 6
	testl	%eax, %eax
	jne	.L54
	.loc 1 151 12
	movq	-32(%rbp), %rax
	jmp	.L55
.L54:
	.loc 1 152 13
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 152 11
	testl	%eax, %eax
	jne	.L56
	.loc 1 153 12
	movq	-24(%rbp), %rax
	jmp	.L55
.L56:
.LBB3:
	.loc 1 156 17
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	.loc 1 156 29
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 156 12
	addl	%edx, %eax
	movl	%eax, -12(%rbp)
	.loc 1 157 41
	movl	-12(%rbp), %eax
	cltq
	addq	$4, %rax
	movq	%rax, %rdi
	call	malloc@PLT
	movq	%rax, -8(%rbp)
	.loc 1 158 15
	movq	-8(%rbp), %rax
	movl	-12(%rbp), %edx
	movl	%edx, (%rax)
	.loc 1 159 12
	movl	$0, -16(%rbp)
	.loc 1 159 5
	jmp	.L57
.L58:
	.loc 1 160 29 discriminator 3
	movq	-24(%rbp), %rdx
	movl	-16(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	.loc 1 160 19 discriminator 3
	movq	-8(%rbp), %rdx
	movl	-16(%rbp), %eax
	cltq
	movb	%cl, 4(%rdx,%rax)
	.loc 1 159 33 discriminator 3
	addl	$1, -16(%rbp)
.L57:
	.loc 1 159 22 discriminator 1
	movq	-24(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 159 19 discriminator 1
	cmpl	%eax, -16(%rbp)
	jl	.L58
	.loc 1 161 12
	movl	$0, -16(%rbp)
	.loc 1 161 5
	jmp	.L59
.L60:
	.loc 1 162 21 discriminator 3
	movq	-24(%rbp), %rax
	movl	(%rax), %edx
	.loc 1 162 18 discriminator 3
	movl	-16(%rbp), %eax
	leal	(%rdx,%rax), %esi
	.loc 1 162 41 discriminator 3
	movq	-32(%rbp), %rdx
	movl	-16(%rbp), %eax
	cltq
	movzbl	4(%rdx,%rax), %ecx
	.loc 1 162 31 discriminator 3
	movq	-8(%rbp), %rdx
	movslq	%esi, %rax
	movb	%cl, 4(%rdx,%rax)
	.loc 1 161 33 discriminator 3
	addl	$1, -16(%rbp)
.L59:
	.loc 1 161 22 discriminator 1
	movq	-32(%rbp), %rax
	movl	(%rax), %eax
	.loc 1 161 19 discriminator 1
	cmpl	%eax, -16(%rbp)
	jl	.L60
	.loc 1 163 12
	movq	-8(%rbp), %rax
.L55:
.LBE3:
	.loc 1 165 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE20:
	.size	concat, .-concat
	.globl	not
	.type	not, @function
not:
.LFB21:
	.loc 1 168 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	movl	%edi, -4(%rbp)
	.loc 1 169 10
	cmpl	$0, -4(%rbp)
	sete	%al
	movzbl	%al, %eax
	.loc 1 170 1
	popq	%rbp
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE21:
	.size	not, .-not
	.globl	__wrap_getchar
	.type	__wrap_getchar, @function
__wrap_getchar:
.LFB22:
	.loc 1 176 1
	.cfi_startproc
	endbr64
	pushq	%rbp
	.cfi_def_cfa_offset 16
	.cfi_offset 6, -16
	movq	%rsp, %rbp
	.cfi_def_cfa_register 6
	subq	$16, %rsp
	.loc 1 176 8
	movq	stdin(%rip), %rax
	movq	%rax, %rdi
	call	getc@PLT
	movl	%eax, -4(%rbp)
	.loc 1 177 5
	cmpl	$-1, -4(%rbp)
	jne	.L64
	.loc 1 177 21 discriminator 1
	leaq	empty(%rip), %rax
	jmp	.L65
.L64:
	.loc 1 178 20
	movl	-4(%rbp), %eax
	cltq
	leaq	0(,%rax,8), %rdx
	leaq	consts(%rip), %rax
	addq	%rdx, %rax
.L65:
	.loc 1 179 1
	leave
	.cfi_def_cfa 7, 8
	ret
	.cfi_endproc
.LFE22:
	.size	__wrap_getchar, .-__wrap_getchar
.Letext0:
	.file 2 "/usr/lib/gcc/x86_64-linux-gnu/11/include/stddef.h"
	.file 3 "/usr/include/x86_64-linux-gnu/bits/types.h"
	.file 4 "/usr/include/x86_64-linux-gnu/bits/types/struct_FILE.h"
	.file 5 "/usr/include/x86_64-linux-gnu/bits/types/FILE.h"
	.file 6 "/usr/include/stdio.h"
	.file 7 "/usr/include/stdlib.h"
	.section	.debug_info,"",@progbits
.Ldebug_info0:
	.long	0x7e1
	.value	0x5
	.byte	0x1
	.byte	0x8
	.long	.Ldebug_abbrev0
	.uleb128 0x18
	.long	.LASF78
	.byte	0x1d
	.long	.LASF0
	.long	.LASF1
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.long	.Ldebug_line0
	.uleb128 0x8
	.long	.LASF9
	.byte	0x2
	.byte	0xd1
	.byte	0x17
	.long	0x3a
	.uleb128 0x5
	.byte	0x8
	.byte	0x7
	.long	.LASF2
	.uleb128 0x5
	.byte	0x4
	.byte	0x7
	.long	.LASF3
	.uleb128 0x19
	.byte	0x8
	.uleb128 0x5
	.byte	0x1
	.byte	0x8
	.long	.LASF4
	.uleb128 0x5
	.byte	0x2
	.byte	0x7
	.long	.LASF5
	.uleb128 0x5
	.byte	0x1
	.byte	0x6
	.long	.LASF6
	.uleb128 0x5
	.byte	0x2
	.byte	0x5
	.long	.LASF7
	.uleb128 0x1a
	.byte	0x4
	.byte	0x5
	.string	"int"
	.uleb128 0x5
	.byte	0x8
	.byte	0x5
	.long	.LASF8
	.uleb128 0x8
	.long	.LASF10
	.byte	0x3
	.byte	0x98
	.byte	0x19
	.long	0x6d
	.uleb128 0x8
	.long	.LASF11
	.byte	0x3
	.byte	0x99
	.byte	0x1b
	.long	0x6d
	.uleb128 0x4
	.long	0x91
	.uleb128 0x5
	.byte	0x1
	.byte	0x6
	.long	.LASF12
	.uleb128 0x1b
	.long	0x91
	.uleb128 0x10
	.long	.LASF50
	.byte	0xd8
	.byte	0x4
	.byte	0x31
	.long	0x223
	.uleb128 0x1
	.long	.LASF13
	.byte	0x4
	.byte	0x33
	.byte	0x7
	.long	0x66
	.byte	0
	.uleb128 0x1
	.long	.LASF14
	.byte	0x4
	.byte	0x36
	.byte	0x9
	.long	0x8c
	.byte	0x8
	.uleb128 0x1
	.long	.LASF15
	.byte	0x4
	.byte	0x37
	.byte	0x9
	.long	0x8c
	.byte	0x10
	.uleb128 0x1
	.long	.LASF16
	.byte	0x4
	.byte	0x38
	.byte	0x9
	.long	0x8c
	.byte	0x18
	.uleb128 0x1
	.long	.LASF17
	.byte	0x4
	.byte	0x39
	.byte	0x9
	.long	0x8c
	.byte	0x20
	.uleb128 0x1
	.long	.LASF18
	.byte	0x4
	.byte	0x3a
	.byte	0x9
	.long	0x8c
	.byte	0x28
	.uleb128 0x1
	.long	.LASF19
	.byte	0x4
	.byte	0x3b
	.byte	0x9
	.long	0x8c
	.byte	0x30
	.uleb128 0x1
	.long	.LASF20
	.byte	0x4
	.byte	0x3c
	.byte	0x9
	.long	0x8c
	.byte	0x38
	.uleb128 0x1
	.long	.LASF21
	.byte	0x4
	.byte	0x3d
	.byte	0x9
	.long	0x8c
	.byte	0x40
	.uleb128 0x1
	.long	.LASF22
	.byte	0x4
	.byte	0x40
	.byte	0x9
	.long	0x8c
	.byte	0x48
	.uleb128 0x1
	.long	.LASF23
	.byte	0x4
	.byte	0x41
	.byte	0x9
	.long	0x8c
	.byte	0x50
	.uleb128 0x1
	.long	.LASF24
	.byte	0x4
	.byte	0x42
	.byte	0x9
	.long	0x8c
	.byte	0x58
	.uleb128 0x1
	.long	.LASF25
	.byte	0x4
	.byte	0x44
	.byte	0x16
	.long	0x23c
	.byte	0x60
	.uleb128 0x1
	.long	.LASF26
	.byte	0x4
	.byte	0x46
	.byte	0x14
	.long	0x241
	.byte	0x68
	.uleb128 0x1
	.long	.LASF27
	.byte	0x4
	.byte	0x48
	.byte	0x7
	.long	0x66
	.byte	0x70
	.uleb128 0x1
	.long	.LASF28
	.byte	0x4
	.byte	0x49
	.byte	0x7
	.long	0x66
	.byte	0x74
	.uleb128 0x1
	.long	.LASF29
	.byte	0x4
	.byte	0x4a
	.byte	0xb
	.long	0x74
	.byte	0x78
	.uleb128 0x1
	.long	.LASF30
	.byte	0x4
	.byte	0x4d
	.byte	0x12
	.long	0x51
	.byte	0x80
	.uleb128 0x1
	.long	.LASF31
	.byte	0x4
	.byte	0x4e
	.byte	0xf
	.long	0x58
	.byte	0x82
	.uleb128 0x1
	.long	.LASF32
	.byte	0x4
	.byte	0x4f
	.byte	0x8
	.long	0x246
	.byte	0x83
	.uleb128 0x1
	.long	.LASF33
	.byte	0x4
	.byte	0x51
	.byte	0xf
	.long	0x256
	.byte	0x88
	.uleb128 0x1
	.long	.LASF34
	.byte	0x4
	.byte	0x59
	.byte	0xd
	.long	0x80
	.byte	0x90
	.uleb128 0x1
	.long	.LASF35
	.byte	0x4
	.byte	0x5b
	.byte	0x17
	.long	0x260
	.byte	0x98
	.uleb128 0x1
	.long	.LASF36
	.byte	0x4
	.byte	0x5c
	.byte	0x19
	.long	0x26a
	.byte	0xa0
	.uleb128 0x1
	.long	.LASF37
	.byte	0x4
	.byte	0x5d
	.byte	0x14
	.long	0x241
	.byte	0xa8
	.uleb128 0x1
	.long	.LASF38
	.byte	0x4
	.byte	0x5e
	.byte	0x9
	.long	0x48
	.byte	0xb0
	.uleb128 0x1
	.long	.LASF39
	.byte	0x4
	.byte	0x5f
	.byte	0xa
	.long	0x2e
	.byte	0xb8
	.uleb128 0x1
	.long	.LASF40
	.byte	0x4
	.byte	0x60
	.byte	0x7
	.long	0x66
	.byte	0xc0
	.uleb128 0x1
	.long	.LASF41
	.byte	0x4
	.byte	0x62
	.byte	0x8
	.long	0x26f
	.byte	0xc4
	.byte	0
	.uleb128 0x8
	.long	.LASF42
	.byte	0x5
	.byte	0x7
	.byte	0x19
	.long	0x9d
	.uleb128 0x1c
	.long	.LASF79
	.byte	0x4
	.byte	0x2b
	.byte	0xe
	.uleb128 0xd
	.long	.LASF43
	.uleb128 0x4
	.long	0x237
	.uleb128 0x4
	.long	0x9d
	.uleb128 0x9
	.long	0x91
	.long	0x256
	.uleb128 0xa
	.long	0x3a
	.byte	0
	.byte	0
	.uleb128 0x4
	.long	0x22f
	.uleb128 0xd
	.long	.LASF44
	.uleb128 0x4
	.long	0x25b
	.uleb128 0xd
	.long	.LASF45
	.uleb128 0x4
	.long	0x265
	.uleb128 0x9
	.long	0x91
	.long	0x27f
	.uleb128 0xa
	.long	0x3a
	.byte	0x13
	.byte	0
	.uleb128 0x11
	.long	.LASF46
	.byte	0x8f
	.long	0x289
	.uleb128 0x4
	.long	0x223
	.uleb128 0x11
	.long	.LASF47
	.byte	0x90
	.long	0x289
	.uleb128 0x5
	.byte	0x8
	.byte	0x5
	.long	.LASF48
	.uleb128 0x5
	.byte	0x8
	.byte	0x7
	.long	.LASF49
	.uleb128 0x10
	.long	.LASF51
	.byte	0x8
	.byte	0x1
	.byte	0x22
	.long	0x2cd
	.uleb128 0x1
	.long	.LASF52
	.byte	0x1
	.byte	0x24
	.byte	0x7
	.long	0x66
	.byte	0
	.uleb128 0x1
	.long	.LASF53
	.byte	0x1
	.byte	0x25
	.byte	0x11
	.long	0x2cd
	.byte	0x4
	.byte	0
	.uleb128 0x9
	.long	0x4a
	.long	0x2dd
	.uleb128 0xa
	.long	0x3a
	.byte	0
	.byte	0
	.uleb128 0x9
	.long	0x2a6
	.long	0x2ed
	.uleb128 0xa
	.long	0x3a
	.byte	0xff
	.byte	0
	.uleb128 0x12
	.long	.LASF54
	.byte	0x5c
	.long	0x2dd
	.uleb128 0x9
	.byte	0x3
	.quad	consts
	.uleb128 0x12
	.long	.LASF55
	.byte	0x5d
	.long	0x2a6
	.uleb128 0x9
	.byte	0x3
	.quad	empty
	.uleb128 0xb
	.long	.LASF56
	.byte	0x6
	.value	0x202
	.byte	0xc
	.long	0x66
	.long	0x32c
	.uleb128 0x6
	.long	0x289
	.byte	0
	.uleb128 0x1d
	.long	.LASF57
	.byte	0x7
	.value	0x270
	.byte	0xd
	.long	0x33f
	.uleb128 0x6
	.long	0x66
	.byte	0
	.uleb128 0x1e
	.long	.LASF80
	.byte	0x1
	.byte	0x67
	.byte	0xa
	.long	0x66
	.long	0x351
	.uleb128 0xe
	.byte	0
	.uleb128 0x1f
	.long	.LASF58
	.byte	0x6
	.byte	0xe6
	.byte	0xc
	.long	0x66
	.long	0x367
	.uleb128 0x6
	.long	0x289
	.byte	0
	.uleb128 0xb
	.long	.LASF59
	.byte	0x6
	.value	0x164
	.byte	0xc
	.long	0x66
	.long	0x37f
	.uleb128 0x6
	.long	0x37f
	.uleb128 0xe
	.byte	0
	.uleb128 0x4
	.long	0x98
	.uleb128 0xb
	.long	.LASF60
	.byte	0x6
	.value	0x22c
	.byte	0xc
	.long	0x66
	.long	0x39b
	.uleb128 0x6
	.long	0x66
	.byte	0
	.uleb128 0xb
	.long	.LASF61
	.byte	0x7
	.value	0x21c
	.byte	0xe
	.long	0x48
	.long	0x3b2
	.uleb128 0x6
	.long	0x2e
	.byte	0
	.uleb128 0x13
	.long	.LASF66
	.byte	0xaf
	.byte	0x10
	.long	0x3e0
	.quad	.LFB22
	.quad	.LFE22-.LFB22
	.uleb128 0x1
	.byte	0x9c
	.long	0x3e0
	.uleb128 0x2
	.string	"i"
	.byte	0xb0
	.byte	0x6
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x4
	.long	0x2a6
	.uleb128 0x14
	.string	"not"
	.byte	0xa7
	.long	0x66
	.quad	.LFB21
	.quad	.LFE21-.LFB21
	.uleb128 0x1
	.byte	0x9c
	.long	0x412
	.uleb128 0x3
	.string	"i"
	.byte	0xa7
	.byte	0xd
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0xc
	.long	.LASF62
	.byte	0x94
	.byte	0x10
	.long	0x3e0
	.quad	.LFB20
	.quad	.LFE20-.LFB20
	.uleb128 0x1
	.byte	0x9c
	.long	0x482
	.uleb128 0x3
	.string	"a"
	.byte	0x94
	.byte	0x26
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x3
	.string	"b"
	.byte	0x94
	.byte	0x38
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x15
	.quad	.LBB3
	.quad	.LBE3-.LBB3
	.uleb128 0x2
	.string	"i"
	.byte	0x9c
	.byte	0x9
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -32
	.uleb128 0x2
	.string	"n"
	.byte	0x9c
	.byte	0xc
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x2
	.string	"t"
	.byte	0x9d
	.byte	0x14
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.byte	0
	.uleb128 0xc
	.long	.LASF63
	.byte	0x81
	.byte	0x10
	.long	0x3e0
	.quad	.LFB19
	.quad	.LFE19-.LFB19
	.uleb128 0x1
	.byte	0x9c
	.long	0x4f4
	.uleb128 0x3
	.string	"s"
	.byte	0x81
	.byte	0x29
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x7
	.long	.LASF64
	.byte	0x81
	.byte	0x30
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -44
	.uleb128 0x3
	.string	"n"
	.byte	0x81
	.byte	0x3b
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x15
	.quad	.LBB2
	.quad	.LBE2-.LBB2
	.uleb128 0x2
	.string	"t"
	.byte	0x8b
	.byte	0x14
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.uleb128 0x2
	.string	"i"
	.byte	0x8c
	.byte	0x9
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.byte	0
	.byte	0
	.uleb128 0xf
	.long	.LASF65
	.byte	0x7c
	.long	0x66
	.quad	.LFB18
	.quad	.LFE18-.LFB18
	.uleb128 0x1
	.byte	0x9c
	.long	0x521
	.uleb128 0x3
	.string	"s"
	.byte	0x7c
	.byte	0x19
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x20
	.string	"chr"
	.byte	0x1
	.byte	0x72
	.byte	0x10
	.long	0x3e0
	.quad	.LFB17
	.quad	.LFE17-.LFB17
	.uleb128 0x1
	.byte	0x9c
	.long	0x550
	.uleb128 0x3
	.string	"i"
	.byte	0x72
	.byte	0x19
	.long	0x6d
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x14
	.string	"ord"
	.byte	0x6a
	.long	0x66
	.quad	.LFB16
	.quad	.LFE16-.LFB16
	.uleb128 0x1
	.byte	0x9c
	.long	0x57d
	.uleb128 0x3
	.string	"s"
	.byte	0x6a
	.byte	0x18
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x13
	.long	.LASF67
	.byte	0x5f
	.byte	0x5
	.long	0x66
	.quad	.LFB15
	.quad	.LFE15-.LFB15
	.uleb128 0x1
	.byte	0x9c
	.long	0x5b9
	.uleb128 0x2
	.string	"i"
	.byte	0x61
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.uleb128 0x21
	.long	.LASF80
	.byte	0x1
	.byte	0x67
	.byte	0xa
	.long	0x66
	.uleb128 0xe
	.byte	0
	.byte	0
	.uleb128 0x16
	.long	.LASF68
	.byte	0x57
	.quad	.LFB14
	.quad	.LFE14-.LFB14
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x16
	.long	.LASF69
	.byte	0x4f
	.quad	.LFB13
	.quad	.LFE13-.LFB13
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x17
	.long	.LASF70
	.byte	0x4a
	.quad	.LFB12
	.quad	.LFE12-.LFB12
	.uleb128 0x1
	.byte	0x9c
	.long	0x612
	.uleb128 0x3
	.string	"n"
	.byte	0x4a
	.byte	0x11
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0x17
	.long	.LASF71
	.byte	0x42
	.quad	.LFB11
	.quad	.LFE11-.LFB11
	.uleb128 0x1
	.byte	0x9c
	.long	0x653
	.uleb128 0x3
	.string	"s"
	.byte	0x42
	.byte	0x1b
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x2
	.string	"i"
	.byte	0x44
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x2
	.string	"p"
	.byte	0x45
	.byte	0x12
	.long	0x653
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x4
	.long	0x4a
	.uleb128 0xf
	.long	.LASF72
	.byte	0x35
	.long	0x66
	.quad	.LFB10
	.quad	.LFE10-.LFB10
	.uleb128 0x1
	.byte	0x9c
	.long	0x69d
	.uleb128 0x3
	.string	"s"
	.byte	0x35
	.byte	0x20
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x3
	.string	"t"
	.byte	0x35
	.byte	0x32
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x2
	.string	"i"
	.byte	0x37
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0xf
	.long	.LASF73
	.byte	0x28
	.long	0x66
	.quad	.LFB9
	.quad	.LFE9-.LFB9
	.uleb128 0x1
	.byte	0x9c
	.long	0x6e2
	.uleb128 0x3
	.string	"s"
	.byte	0x28
	.byte	0x1a
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x3
	.string	"t"
	.byte	0x28
	.byte	0x2c
	.long	0x3e0
	.uleb128 0x2
	.byte	0x91
	.sleb128 -48
	.uleb128 0x2
	.string	"i"
	.byte	0x2a
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -20
	.byte	0
	.uleb128 0xc
	.long	.LASF74
	.byte	0x18
	.byte	0x7
	.long	0x736
	.quad	.LFB8
	.quad	.LFE8-.LFB8
	.uleb128 0x1
	.byte	0x9c
	.long	0x736
	.uleb128 0x7
	.long	.LASF65
	.byte	0x18
	.byte	0x16
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -52
	.uleb128 0x2
	.string	"i"
	.byte	0x1a
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0x2
	.string	"p"
	.byte	0x1b
	.byte	0x9
	.long	0x736
	.uleb128 0x2
	.byte	0x91
	.sleb128 -32
	.uleb128 0x2
	.string	"a"
	.byte	0x1b
	.byte	0xd
	.long	0x736
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x4
	.long	0x6d
	.uleb128 0xc
	.long	.LASF75
	.byte	0xd
	.byte	0x7
	.long	0x736
	.quad	.LFB7
	.quad	.LFE7-.LFB7
	.uleb128 0x1
	.byte	0x9c
	.long	0x791
	.uleb128 0x7
	.long	.LASF65
	.byte	0xd
	.byte	0x22
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0x7
	.long	.LASF76
	.byte	0xd
	.byte	0x2c
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x2
	.string	"i"
	.byte	0xf
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x2
	.string	"a"
	.byte	0x10
	.byte	0x9
	.long	0x736
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.uleb128 0x22
	.long	.LASF77
	.byte	0x1
	.byte	0x4
	.byte	0x7
	.long	0x736
	.quad	.LFB6
	.quad	.LFE6-.LFB6
	.uleb128 0x1
	.byte	0x9c
	.uleb128 0x7
	.long	.LASF65
	.byte	0x4
	.byte	0x15
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -36
	.uleb128 0x7
	.long	.LASF76
	.byte	0x4
	.byte	0x1f
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -40
	.uleb128 0x2
	.string	"i"
	.byte	0x6
	.byte	0x7
	.long	0x66
	.uleb128 0x2
	.byte	0x91
	.sleb128 -28
	.uleb128 0x2
	.string	"a"
	.byte	0x7
	.byte	0x9
	.long	0x736
	.uleb128 0x2
	.byte	0x91
	.sleb128 -24
	.byte	0
	.byte	0
	.section	.debug_abbrev,"",@progbits
.Ldebug_abbrev0:
	.uleb128 0x1
	.uleb128 0xd
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x38
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x2
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x3
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x4
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0x21
	.sleb128 8
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x5
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0xe
	.byte	0
	.byte	0
	.uleb128 0x6
	.uleb128 0x5
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x7
	.uleb128 0x5
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x8
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x9
	.uleb128 0x1
	.byte	0x1
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xa
	.uleb128 0x21
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x2f
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0xb
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xc
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0xd
	.uleb128 0x13
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0xe
	.uleb128 0x18
	.byte	0
	.byte	0
	.byte	0
	.uleb128 0xf
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 5
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7a
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x10
	.uleb128 0x13
	.byte	0x1
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 8
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x11
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 6
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 14
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x12
	.uleb128 0x34
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 15
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x2
	.uleb128 0x18
	.byte	0
	.byte	0
	.uleb128 0x13
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x14
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 5
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7a
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x15
	.uleb128 0xb
	.byte	0x1
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.byte	0
	.byte	0
	.uleb128 0x16
	.uleb128 0x2e
	.byte	0
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 6
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x17
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0x21
	.sleb128 1
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0x21
	.sleb128 6
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x18
	.uleb128 0x11
	.byte	0x1
	.uleb128 0x25
	.uleb128 0xe
	.uleb128 0x13
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0x1f
	.uleb128 0x1b
	.uleb128 0x1f
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x10
	.uleb128 0x17
	.byte	0
	.byte	0
	.uleb128 0x19
	.uleb128 0xf
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x1a
	.uleb128 0x24
	.byte	0
	.uleb128 0xb
	.uleb128 0xb
	.uleb128 0x3e
	.uleb128 0xb
	.uleb128 0x3
	.uleb128 0x8
	.byte	0
	.byte	0
	.uleb128 0x1b
	.uleb128 0x26
	.byte	0
	.uleb128 0x49
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1c
	.uleb128 0x16
	.byte	0
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.byte	0
	.byte	0
	.uleb128 0x1d
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0x5
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x87
	.uleb128 0x19
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1e
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x1f
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x20
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0x8
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.uleb128 0x1
	.uleb128 0x13
	.byte	0
	.byte	0
	.uleb128 0x21
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x3c
	.uleb128 0x19
	.byte	0
	.byte	0
	.uleb128 0x22
	.uleb128 0x2e
	.byte	0x1
	.uleb128 0x3f
	.uleb128 0x19
	.uleb128 0x3
	.uleb128 0xe
	.uleb128 0x3a
	.uleb128 0xb
	.uleb128 0x3b
	.uleb128 0xb
	.uleb128 0x39
	.uleb128 0xb
	.uleb128 0x27
	.uleb128 0x19
	.uleb128 0x49
	.uleb128 0x13
	.uleb128 0x11
	.uleb128 0x1
	.uleb128 0x12
	.uleb128 0x7
	.uleb128 0x40
	.uleb128 0x18
	.uleb128 0x7c
	.uleb128 0x19
	.byte	0
	.byte	0
	.byte	0
	.section	.debug_aranges,"",@progbits
	.long	0x2c
	.value	0x2
	.long	.Ldebug_info0
	.byte	0x8
	.byte	0
	.value	0
	.value	0
	.quad	.Ltext0
	.quad	.Letext0-.Ltext0
	.quad	0
	.quad	0
	.section	.debug_line,"",@progbits
.Ldebug_line0:
	.section	.debug_str,"MS",@progbits,1
.LASF59:
	.string	"printf"
.LASF10:
	.string	"__off_t"
.LASF14:
	.string	"_IO_read_ptr"
.LASF61:
	.string	"malloc"
.LASF26:
	.string	"_chain"
.LASF71:
	.string	"print"
.LASF9:
	.string	"size_t"
.LASF32:
	.string	"_shortbuf"
.LASF51:
	.string	"string"
.LASF76:
	.string	"init"
.LASF20:
	.string	"_IO_buf_base"
.LASF49:
	.string	"long long unsigned int"
.LASF70:
	.string	"printi"
.LASF77:
	.string	"initArray"
.LASF69:
	.string	"rttest"
.LASF35:
	.string	"_codecvt"
.LASF48:
	.string	"long long int"
.LASF6:
	.string	"signed char"
.LASF74:
	.string	"initRecord"
.LASF73:
	.string	"streq"
.LASF27:
	.string	"_fileno"
.LASF78:
	.string	"GNU C17 11.4.0 -mtune=generic -march=x86-64 -g -fasynchronous-unwind-tables -fstack-protector-strong -fstack-clash-protection -fcf-protection"
.LASF15:
	.string	"_IO_read_end"
.LASF55:
	.string	"empty"
.LASF8:
	.string	"long int"
.LASF13:
	.string	"_flags"
.LASF21:
	.string	"_IO_buf_end"
.LASF30:
	.string	"_cur_column"
.LASF60:
	.string	"putchar"
.LASF44:
	.string	"_IO_codecvt"
.LASF29:
	.string	"_old_offset"
.LASF34:
	.string	"_offset"
.LASF62:
	.string	"concat"
.LASF43:
	.string	"_IO_marker"
.LASF46:
	.string	"stdin"
.LASF3:
	.string	"unsigned int"
.LASF38:
	.string	"_freeres_buf"
.LASF56:
	.string	"getc"
.LASF80:
	.string	"tigermain"
.LASF2:
	.string	"long unsigned int"
.LASF18:
	.string	"_IO_write_ptr"
.LASF65:
	.string	"size"
.LASF5:
	.string	"short unsigned int"
.LASF22:
	.string	"_IO_save_base"
.LASF63:
	.string	"substring"
.LASF33:
	.string	"_lock"
.LASF28:
	.string	"_flags2"
.LASF40:
	.string	"_mode"
.LASF47:
	.string	"stdout"
.LASF75:
	.string	"initArrayBoundsChecked"
.LASF19:
	.string	"_IO_write_end"
.LASF79:
	.string	"_IO_lock_t"
.LASF50:
	.string	"_IO_FILE"
.LASF58:
	.string	"fflush"
.LASF25:
	.string	"_markers"
.LASF4:
	.string	"unsigned char"
.LASF7:
	.string	"short int"
.LASF45:
	.string	"_IO_wide_data"
.LASF31:
	.string	"_vtable_offset"
.LASF42:
	.string	"FILE"
.LASF57:
	.string	"exit"
.LASF72:
	.string	"stringEqual"
.LASF64:
	.string	"first"
.LASF52:
	.string	"length"
.LASF12:
	.string	"char"
.LASF66:
	.string	"__wrap_getchar"
.LASF11:
	.string	"__off64_t"
.LASF16:
	.string	"_IO_read_base"
.LASF24:
	.string	"_IO_save_end"
.LASF39:
	.string	"__pad5"
.LASF68:
	.string	"flush"
.LASF41:
	.string	"_unused2"
.LASF54:
	.string	"consts"
.LASF23:
	.string	"_IO_backup_base"
.LASF53:
	.string	"chars"
.LASF37:
	.string	"_freeres_list"
.LASF36:
	.string	"_wide_data"
.LASF67:
	.string	"main"
.LASF17:
	.string	"_IO_write_base"
	.section	.debug_line_str,"MS",@progbits,1
.LASF1:
	.string	"/usr/src/app/compiler/src/main/resources/runtimes"
.LASF0:
	.string	"runtime.c"
	.ident	"GCC: (Ubuntu 11.4.0-1ubuntu1~22.04) 11.4.0"
	.section	.note.GNU-stack,"",@progbits
	.section	.note.gnu.property,"a"
	.align 8
	.long	1f - 0f
	.long	4f - 1f
	.long	5
0:
	.string	"GNU"
1:
	.align 8
	.long	0xc0000002
	.long	3f - 2f
2:
	.long	0x3
3:
	.align 8
4:
