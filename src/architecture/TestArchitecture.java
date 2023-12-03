package architecture;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import components.Memory;

public class TestArchitecture {
	
	//uncomment the anotation below to run the architecture showing components status
	//@Test
	public void testShowComponentes() {

		//a complete test (for visual purposes only).
		//a single code as follows
//		ldi 2
//		store 40
//		ldi -4
//		point:
//		store 41  //mem[41]=-4 (then -3, -2, -1, 0)
//		read 40
//		add 40    //mem[40] + mem[40]
//		store 40  //result must be in 40
//		read 41
//		inc
//		jn point
//		end
		
		Architecture arch = new Architecture(true);
		arch.getMemory().getDataList()[0]=7;
		arch.getMemory().getDataList()[1]=2;
		arch.getMemory().getDataList()[2]=6;
		arch.getMemory().getDataList()[3]=40;
		arch.getMemory().getDataList()[4]=7;
		arch.getMemory().getDataList()[5]=-4;
		arch.getMemory().getDataList()[6]=6;
		arch.getMemory().getDataList()[7]=41;
		arch.getMemory().getDataList()[8]=5;
		arch.getMemory().getDataList()[9]=40;
		arch.getMemory().getDataList()[10]=0;
		arch.getMemory().getDataList()[11]=40;
		arch.getMemory().getDataList()[12]=6;
		arch.getMemory().getDataList()[13]=40;
		arch.getMemory().getDataList()[14]=5;
		arch.getMemory().getDataList()[15]=41;
		arch.getMemory().getDataList()[16]=8;
		arch.getMemory().getDataList()[17]=4;
		arch.getMemory().getDataList()[18]=6;
		arch.getMemory().getDataList()[19]=-1;
		arch.getMemory().getDataList()[40]=0;
		arch.getMemory().getDataList()[41]=0;
		//now the program and the variables are stored. we can run
		arch.controlUnitEexec();
		
	}

//	@Test
	public void testAddRR() {
		Architecture arch = new Architecture();
		
		//storing the number 8 in the RPG0
		arch.getExtbus1().put(8);
		arch.getDemux().setValue(0);
		arch.registersStore();
		
		//storing the number 2 in the RPG1
		arch.getExtbus1().put(2);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		//storing the number 1(rgp1) in position 11
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 0(rgp0) in position 12
		arch.getExtbus1().put(12);
		arch.getMemory().store();
		arch.getExtbus1().put(0); 
		arch.getMemory().store();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();      

		arch.addRR();
		arch.registersRead();
		
		//the bus must contains the number 10
		System.out.println(arch.getExtbus1().get());
		assertEquals(10, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(13, arch.getExtbus1().get());

	}
	
//	@Test
	public void testAddMR() {
		Architecture arch = new Architecture();
		
		//storing the number 8 in the position 10
		arch.getExtbus1().put(10);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 11
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 8 in the position 8
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 2 in the RPG1
		arch.getExtbus1().put(2);
		arch.getDemux().setValue(1);
		arch.registersStore();		
		
		//PC points to position 9
		arch.getExtbus1().put(9);
		arch.getPc().store();      

		arch.addMR();
		arch.registersRead();
		
		//the bus must contains the number 10
		System.out.println(arch.getExtbus1().get());
		assertEquals(10, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));
		//PC must be pointing to 12
		arch.getPc().read();
		assertEquals(12, arch.getExtbus1().get());

	}

//	@Test
	public void testAddRM() {
		Architecture arch = new Architecture();
		
		//storing the number 8 in the position 11
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 10
		arch.getExtbus1().put(10);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 8 in the position 8
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 2 in the RPG1
		arch.getExtbus1().put(2);
		arch.getDemux().setValue(1);
		arch.registersStore();		
		
		//PC points to position 9
		arch.getExtbus1().put(9);
		arch.getPc().store();      

		arch.addRM();
		
		arch.getExtbus1().put(8);
		arch.getMemory().read();
		
		//the bus must contains the number 10
		System.out.println(arch.getExtbus1().get());
		assertEquals(10, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));
		//PC must be pointing to 12
		arch.getPc().read();
		assertEquals(12, arch.getExtbus1().get());

	}

//	@Test
	public void testSubRR() {
		Architecture arch = new Architecture();
		
		//storing the number 8 in the RPG0
		arch.getExtbus1().put(8);
		arch.getDemux().setValue(0);
		arch.registersStore();
		
		//storing the number 2 in the RPG1
		arch.getExtbus1().put(2);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		//storing the number 1(rgp1) in position 11
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 0(rgp0) in position 12
		arch.getExtbus1().put(12);
		arch.getMemory().store();
		arch.getExtbus1().put(0); 
		arch.getMemory().store();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();      

		arch.subRR();
		arch.registersRead();
		
		//the bus must contains the number 10
		System.out.println(arch.getExtbus1().get());
		assertEquals(-6, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(1, arch.getFlags().getBit(1));
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(13, arch.getExtbus1().get());

	}
	
//	@Test
	public void testSubMR() {
		Architecture arch = new Architecture();
		
		//storing the number 8 in the position 10
		arch.getExtbus1().put(10);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 11
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 8 in the position 8
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 2 in the RPG1
		arch.getExtbus1().put(2);
		arch.getDemux().setValue(1);
		arch.registersStore();		
		
		//PC points to position 9
		arch.getExtbus1().put(9);
		arch.getPc().store();      

		arch.subMR();
		arch.registersRead();
		
		//the bus must contains the number 10
		System.out.println(arch.getExtbus1().get());
		assertEquals(6, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(0, arch.getFlags().getBit(1));
		//PC must be pointing to 12
		arch.getPc().read();
		assertEquals(12, arch.getExtbus1().get());

	}

//	@Test
	public void testSubRM() {
		Architecture arch = new Architecture();
		
		//storing the number 8 in the position 11
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 10
		arch.getExtbus1().put(10);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 8 in the position 8
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		//storing the number 2 in the RPG1
		arch.getExtbus1().put(2);
		arch.getDemux().setValue(1);
		arch.registersStore();		
		
		//PC points to position 9
		arch.getExtbus1().put(9);
		arch.getPc().store();      

		arch.subRM();
		
		arch.getExtbus1().put(8);
		arch.getMemory().read();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(-6, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		assertEquals(0, arch.getFlags().getBit(0));
		assertEquals(1, arch.getFlags().getBit(1));
		//PC must be pointing to 12
		arch.getPc().read();
		assertEquals(12, arch.getExtbus1().get());

	}
	
//	@Test
	public void testmoveMR() {
		Architecture arch = new Architecture();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		
		arch.getExtbus1().put(8);
		arch.getMemory().store();
		arch.getExtbus1().put(15);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 12
		arch.getExtbus1().put(12);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		
		arch.moveMR();
		
		arch.getDemux().setValue(1);
		arch.registersRead();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(15, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(13, arch.getExtbus1().get());
	}

//	@Test
	public void testmoveRM() {
		
		Architecture arch = new Architecture();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();
		
		arch.getExtbus1().put(21);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 12
		arch.getExtbus1().put(12);
		arch.getMemory().store();
		arch.getExtbus1().put(7);
		arch.getMemory().store();
		
		arch.moveRM();
		
		arch.getExtbus1().put(7);
		arch.getMemory().read();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(21, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(13, arch.getExtbus1().get());
		
	}
	
//	@Test
	public void moveRR() {
		Architecture arch = new Architecture();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();
		
		arch.getExtbus1().put(21);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 12
		arch.getExtbus1().put(12);
		arch.getMemory().store();
		arch.getExtbus1().put(2);
		arch.getMemory().store();
		
		
		arch.moveRR();
		
		arch.getDemux().setValue(1);
		arch.registersRead();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(21, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(13, arch.getExtbus1().get());
	}
	
//	@Test
	public void testmoveIR() {
		Architecture arch = new Architecture();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(16);
		arch.getMemory().store();
		
		//storing the number 1(rgp1) in position 12
		arch.getExtbus1().put(12);
		arch.getMemory().store();
		arch.getExtbus1().put(2);
		arch.getMemory().store();
		
		
		arch.moveIR();
		
		arch.getDemux().setValue(2);
		arch.registersRead();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(16, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(13, arch.getExtbus1().get());
	}
	
//	@Test
	public void testIncR() {
		
		Architecture arch = new Architecture();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();
		
		arch.getExtbus1().put(21);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		arch.IncR();
		
		arch.getDemux().setValue(1);
		arch.registersRead();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(22, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(12, arch.getExtbus1().get());
	}
	
//	@Test
	public void testIncM() {
		Architecture arch = new Architecture();
		
		//PC points to position 10
		arch.getExtbus1().put(10);
		arch.getPc().store();
		
		arch.getExtbus1().put(11);
		arch.getMemory().store();
		arch.getExtbus1().put(7);
		arch.getMemory().store();
		
		arch.getExtbus1().put(7);
		arch.getMemory().store();
		arch.getExtbus1().put(17);
		arch.getMemory().store();
		
		arch.IncM();
		
		arch.getExtbus1().put(7);
		arch.getMemory().read();
		
		System.out.println(arch.getExtbus1().get());
		assertEquals(18, arch.getExtbus1().get());
		//the flags bits 0 and 1 must be 0
		
		//PC must be pointing to 13
		arch.getPc().read();
		assertEquals(12, arch.getExtbus1().get());
	}
	
//	@Test
	public void testJmp() {
		Architecture arch = new Architecture();
		//storing the number 10 in PC
		arch.getIntbus2().put(10);
		arch.getPc().internalStore();

		//storing the number 25 in the memory, in the position just before that one adressed by PC
		arch.getExtbus1().put(11); //the position is 11, once PC points to 10
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();
		
		
		//testing if PC stores the number 10
		arch.getPc().read();
		assertEquals(10, arch.getExtbus1().get());
		
		//now we can perform the jmp method. 
		//we will move the the number 25 (stored in the 31th position in the memory) 
		//into the PC
		arch.jmp();
		arch.getPc().internalRead();;
		//the internalbus2 must contains the number 25
		assertEquals(25, arch.getIntbus2().get());

	}
	
//	@Test
	public void testJn() {
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus1().put(30);
		arch.getPc().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();

		//now we can perform the jn method. 

		//CASE 1.
		//Bit NEGATIVE is equals to 1
		arch.getFlags().setBit(1, 1);
		
		//So, we will move the the number 25 (stored in the 31th position in the memory) 
		//into the PC
		arch.jn();
		//testing if PC stores the number 30
		arch.getPc().read();
		assertEquals(25, arch.getExtbus1().get());		

		
		//CASE 2.
		//Bit NEGATIVE is equals to 0
		arch.getFlags().setBit(1, 0);
		//PC must have the number 30 initially (in this time, by using the external bus)
		arch.getExtbus1().put(30);
		arch.getPc().store();
		//destroying the data in external bus

		//testing if PC stores the number 30
		
		//Note that the memory was not changed. So, in position 31 we have the number 25
		
		//Once the ZERO bit is 0, we WILL NOT move the number 25 (stored in the 31th position in the memory)
		//into the PC.
		//The original PC position was 30. The parameter is in position 31. So, now PC must be pointing to 32
		arch.jn();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(32, arch.getIntbus2().get());
	}
	
	
	
//	@Test
	public void testJz() {
Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus1().put(30);
		arch.getPc().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();

		//now we can perform the jn method. 

		//CASE 1.
		//Bit NEGATIVE is equals to 1
		arch.getFlags().setBit(0, 1);
		
		//So, we will move the the number 25 (stored in the 31th position in the memory) 
		//into the PC
		arch.jz();
		//testing if PC stores the number 30
		arch.getPc().read();
		assertEquals(25, arch.getExtbus1().get());		

		
		//CASE 2.
		//Bit NEGATIVE is equals to 0
		arch.getFlags().setBit(0, 0);
		//PC must have the number 30 initially (in this time, by using the external bus)
		arch.getExtbus1().put(30);
		arch.getPc().store();
		//destroying the data in external bus

		//testing if PC stores the number 30
		
		//Note that the memory was not changed. So, in position 31 we have the number 25
		
		//Once the ZERO bit is 0, we WILL NOT move the number 25 (stored in the 31th position in the memory)
		//into the PC.
		//The original PC position was 30. The parameter is in position 31. So, now PC must be pointing to 32
		arch.jz();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(32, arch.getIntbus2().get());
	}
	//@Test QUE BELEZA
	public void testJnz() {
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus1().put(30);
		arch.getPc().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();

		//now we can perform the jn method. 

		//CASE 1.
		//Bit NEGATIVE is equals to 1
		arch.getFlags().setBit(0, 1);
		
		//So, we will move the the number 25 (stored in the 31th position in the memory) 
		//into the PC
		arch.jnz();
		//testing if PC stores the number 30
		arch.getPc().read();
		assertEquals(32, arch.getExtbus1().get());		

		
		//CASE 2.
		//Bit NEGATIVE is equals to 0
		arch.getFlags().setBit(0, 0);
		//PC must have the number 30 initially (in this time, by using the external bus)
		arch.getExtbus1().put(30);
		arch.getPc().store();
		//destroying the data in external bus

		//testing if PC stores the number 30
		
		//Note that the memory was not changed. So, in position 31 we have the number 25
		
		//Once the ZERO bit is 0, we WILL NOT move the number 25 (stored in the 31th position in the memory)
		//into the PC.
		//The original PC position was 30. The parameter is in position 31. So, now PC must be pointing to 32
		arch.jnz();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(25, arch.getIntbus2().get());
	}
	//@Test JOIA 
	public void testJeq() {
		
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus1().put(30);
		arch.getPc().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(0);
		arch.getMemory().store();
		
		arch.getExtbus1().put(32);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		arch.getExtbus1().put(33);
		arch.getMemory().store();
		arch.getExtbus1().put(50);
		arch.getMemory().store();
		
		arch.getExtbus1().put(10);
		arch.getDemux().setValue(0);
		arch.registersStore();
		
		arch.getDemux().setValue(1);
		arch.registersStore();

		
		arch.jeq();
		//testing if PC stores the number 30
		arch.getPc().read();
		assertEquals(50, arch.getExtbus1().get());		

		
		//CASE 2.

		arch.getExtbus1().put(30);
		arch.getPc().store();
		
		arch.getExtbus1().put(11);
		arch.getDemux().setValue(0);
		arch.registersStore();
		
		arch.jeq();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(34, arch.getIntbus2().get());
		
	}
	
//	@Test
	public void testJgt() {
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus1().put(30);
		arch.getPc().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(0);
		arch.getMemory().store();
		
		arch.getExtbus1().put(32);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		arch.getExtbus1().put(33);
		arch.getMemory().store();
		arch.getExtbus1().put(50);
		arch.getMemory().store();
		
		arch.getExtbus1().put(12);
		arch.getDemux().setValue(0);
		arch.registersStore();
		
		arch.getExtbus1().put(10);
		arch.getDemux().setValue(1);
		arch.registersStore();

		
		arch.jgt();
		//testing if PC stores the number 30
		arch.getPc().read();
		assertEquals(50, arch.getExtbus1().get());		

		
		//CASE 2.

		arch.getExtbus1().put(30);
		arch.getPc().store();
		
		arch.getExtbus1().put(12);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.jgt();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(34, arch.getIntbus2().get());
		
		//CASE 3.
		arch.getExtbus1().put(30);
		arch.getPc().store();
		
		arch.getExtbus1().put(20);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.jgt();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(34, arch.getIntbus2().get());
		
	}
	
	@Test
	public void testJlw() {
		
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus1().put(30);
		arch.getPc().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(0);
		arch.getMemory().store();
		
		arch.getExtbus1().put(32);
		arch.getMemory().store();
		arch.getExtbus1().put(1);
		arch.getMemory().store();
		
		arch.getExtbus1().put(33);
		arch.getMemory().store();
		arch.getExtbus1().put(50);
		arch.getMemory().store();
		
		arch.getExtbus1().put(12);
		arch.getDemux().setValue(0);
		arch.registersStore();
		
		arch.getExtbus1().put(10);
		arch.getDemux().setValue(1);
		arch.registersStore();

		
		arch.jlw();
		//testing if PC stores the number 30
		arch.getPc().read();
		assertEquals(34, arch.getExtbus1().get());		

		
		//CASE 2.

		arch.getExtbus1().put(30);
		arch.getPc().store();
		
		arch.getExtbus1().put(12);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.jlw();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(34, arch.getIntbus2().get());
		
		//CASE 3.
		arch.getExtbus1().put(30);
		arch.getPc().store();
		
		arch.getExtbus1().put(20);
		arch.getDemux().setValue(1);
		arch.registersStore();
		
		arch.jlw();
		//PC contains the number 32
		arch.getPc().internalRead();
		assertEquals(50, arch.getIntbus2().get());
		
	}
		
//	@Test
	public void testFillCommandsList() {
		
		//all the instructions must be in Commands List
		/*
		 *
				add addr (rpg <- rpg + addr)
				sub addr (rpg <- rpg - addr)
				jmp addr (pc <- addr)
				jz addr  (se bitZero pc <- addr)
				jn addr  (se bitneg pc <- addr)
				read addr (rpg <- addr)
				store addr  (addr <- rpg)
				ldi x    (rpg <- x. x must be an integer)
				inc    (rpg++)
				move %reg0 %reg1 (reg1 <- Reg0)
		 */

		
		
		Architecture arch = new Architecture();
		ArrayList<String> commands = arch.getCommandsList();
		assertTrue("add".equals(commands.get(0)));
		assertTrue("sub".equals(commands.get(1)));
		assertTrue("jmp".equals(commands.get(2)));
		assertTrue("jz".equals(commands.get(3)));
		assertTrue("jn".equals(commands.get(4)));
		assertTrue("read".equals(commands.get(5)));
		assertTrue("store".equals(commands.get(6)));
		assertTrue("ldi".equals(commands.get(7)));
		assertTrue("inc".equals(commands.get(8)));
		assertTrue("moveRegReg".equals(commands.get(9)));
	}
	
//	@Test
	public void testReadExec() throws IOException {
		Architecture arch = new Architecture();
		arch.readExec("testFile");
		assertEquals(5, arch.getMemory().getDataList()[0]);
		assertEquals(4, arch.getMemory().getDataList()[1]);
		assertEquals(3, arch.getMemory().getDataList()[2]);
		assertEquals(2, arch.getMemory().getDataList()[3]);
		assertEquals(1, arch.getMemory().getDataList()[4]);
		assertEquals(0, arch.getMemory().getDataList()[5]);
	}

}
