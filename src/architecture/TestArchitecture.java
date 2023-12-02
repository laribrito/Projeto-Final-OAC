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
	public void testJmp() {
		Architecture arch = new Architecture();
		//storing the number 10 in PC
		arch.getIntbus2().put(10);
		arch.getPC().internalStore();

		//storing the number 25 in the memory, in the position just before that one adressed by PC
		arch.getExtbus1().put(11); //the position is 11, once PC points to 10
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();
		
		
		//testing if PC stores the number 10
		arch.getPC().read();
		assertEquals(10, arch.getExtbus1().get());
		
		//now we can perform the jmp method. 
		//we will move the the number 25 (stored in the 31th position in the memory) 
		//into the PC
		arch.jmp();
		arch.getPC().internalRead();;
		//the internalbus2 must contains the number 25
		assertEquals(25, arch.getIntbus2().get());

	}
	
//	@Test
	public void testJz() {
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus2().put(30);
		arch.getPC().internalStore();
		
		//storing the number 25 in the into the memory, in position 31, the position just after PC
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();

		//now we can perform the jz method. 

		//CASE 1.
		//Bit ZERO is equals to 1
		arch.getFlags().setBit(0, 1);
		
		//So, we will move the the number 25 (stored in the 31th position in the memory) 
		//into the PC

		//testing if PC stores the number 30
		arch.getPC().read();
		assertEquals(30, arch.getExtbus1().get());		

		arch.jz();
		
		//PC must be storing the number 25
		arch.getPC().internalRead();
		assertEquals(25, arch.getIntbus2().get());
		
		//CASE 2.
		//Bit ZERO is equals to 0
		arch.getFlags().setBit(0, 0);
		//PC must have the number 30 initially (in this time, by using the external bus)
		arch.getExtbus1().put(30);
		arch.getPC().store();
		//destroying the data in external bus
		arch.getExtbus1().put(0);

		//testing if PC stores the number 30
		arch.getPC().read();
		assertEquals(30, arch.getExtbus1().get());	
		
		//Note that the memory was not changed. So, in position 31 we have the number 25
		
		//Once the ZERO bit is 0, we WILL NOT move the number 25 (stored in the 31th position in the memory)
		//into the PC.
		//The original PC position was 30. The parameter is in position 31. So, now PC must be pointing to 32
		arch.jz();
		//PC contains the number 32
		arch.getPC().internalRead();
		assertEquals(32, arch.getIntbus2().get());
	}
	
//	@Test
	public void testJn() {
		Architecture arch = new Architecture();
		
		//storing the number 30 in PC
		arch.getIntbus2().put(30);
		arch.getPC().internalStore();
		
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

		//testing if PC stores the number 30
		arch.getPC().read();
		assertEquals(30, arch.getExtbus1().get());		

		arch.jn();
		
		//PC must be storng the number 25
		arch.getPC().internalRead();
		assertEquals(25, arch.getIntbus2().get());
		
		//CASE 2.
		//Bit NEGATIVE is equals to 0
		arch.getFlags().setBit(1, 0);
		//PC must have the number 30 initially (in this time, by using the external bus)
		arch.getExtbus1().put(30);
		arch.getPC().store();
		//destroying the data in external bus
		arch.getExtbus1().put(0);

		//testing if PC stores the number 30
		arch.getPC().read();
		assertEquals(30, arch.getExtbus1().get());	
		
		//Note that the memory was not changed. So, in position 31 we have the number 25
		
		//Once the ZERO bit is 0, we WILL NOT move the number 25 (stored in the 31th position in the memory)
		//into the PC.
		//The original PC position was 30. The parameter is in position 31. So, now PC must be pointing to 32
		arch.jn();
		//PC contains the number 32
		arch.getPC().internalRead();
		assertEquals(32, arch.getIntbus2().get());
	}
	
//	@Test
	public void testRead() {
		Architecture arch = new Architecture();
		//storing the number 10 in RPG
		arch.getIntbus1().put(10);
		arch.getRPG().internalStore();
		//testing if RPG stores the number 10
		arch.getRPG().read();
		assertEquals(10, arch.getExtbus1().get());
		
		//storing the number 25 in the memory, in position 31
		arch.getExtbus1().put(31);
		arch.getMemory().store();
		arch.getExtbus1().put(25);
		arch.getMemory().store();
		
		//storing the number -100 in the memory, in position 25
		arch.getExtbus1().put(25);
		arch.getMemory().store();
		arch.getExtbus1().put(-100);
		arch.getMemory().store();
		
		//PC must be pointing to the address just before the parameter (where is the instruction)
		arch.getExtbus1().put(30);
		arch.getPC().store();

		//now we can perform the read method. 
		//scenery PC=30, mem[31]=25, mem[25]=-100 RPG=10
		//we will move the the number 25 (stored in the 31th position in the memory) 
		//into the RPG by using the move command (move 31)

		arch.read();
		arch.getRPG().internalRead();
		//the internalbus1 must contain the number -100 (that is stored in position 25)
		assertEquals(-100, arch.getRPG().getData());
		
		//PC must be pointing two positions after its original value
		arch.getPC().read();
		assertEquals(32, arch.getExtbus1().get());

	}
	
//	@Test
	public void testStore() {
		Architecture arch = new Architecture();

		//storing the number 25 in the memory, in position 31
		arch.getMemory().getDataList()[31]=25;
		

		//now we can perform the store method. 
		//store X stores, in the position X, the data that is currently in RPG
		
		//let's put PC pointing to the position 11
		arch.getExtbus1().put(11);
		arch.getPC().store();
		//now lets put the parameter (the position where the data will be stored) into the position next to PC
		arch.getMemory().getDataList()[12]=31;
		
		//storing the number 155 in RPG
		arch.getIntbus1().put(155);
		arch.getRPG().internalStore();
		
		//testing if memory contains the number 25 in the 31th position
		arch.getExtbus1().put(31);
		arch.getMemory().read();
		assertEquals(25, arch.getExtbus1().get());
		
		//So, PC is pointing to memory[11], memory[12] has 31, memory [31] has 25 and RPG has 155
		
		//destroying data in externalbus 1
		arch.getExtbus1().put(0);

		arch.store();

		//now, memory[31] must be updated from 25 to 155
		assertEquals(155, arch.getMemory().getDataList()[31]);
		
		//PC must be pointing two positions after its original value
		arch.getPC().read();
		assertEquals(13, arch.getExtbus1().get());


	}
	
//	@Test
	public void testLdi() {
		Architecture arch = new Architecture();
		//storing the number 10 in RPG
		arch.getIntbus1().put(10);
		arch.getRPG().internalStore();
		
		//the scenery: PC points to 50, mem[51] (parameter) is -40
		
		arch.getExtbus1().put(51);
		arch.getMemory().store();
		arch.getExtbus1().put(-40);
		arch.getMemory().store();
		
		arch.getExtbus1().put(50);
		arch.getPC().store();
		
		//destroying data in internalbus 1
		arch.getIntbus1().put(0);

		//now we can perform the ldi method. 
		//we will move the the number -40 (immediate value) 
		//into the rpg
		arch.ldi();
		
		arch.getRPG().read();
		//the externalbus1 must contains the number 44
		assertEquals(-40, arch.getExtbus1().get());
		
		//PC must be pointing two positions after its original value
		arch.getPC().read();
		assertEquals(52, arch.getExtbus1().get());

	}
	
//	@Test
	public void testInc() {
		Architecture arch = new Architecture();
		//storing the number 10 in RPG
		arch.getExtbus1().put(10);
		arch.getRPG().store();
		//testing if RPG stores the number 10
		arch.getRPG().read();
		assertEquals(10, arch.getExtbus1().get());

		//destroying data in externalbus 1
		arch.getExtbus1().put(0);
		
		//pc points to 50 (where we suppose the instruction is
		arch.getExtbus1().put(50);
		arch.getPC().store();

		//now we can perform the inc method. 
		arch.inc();
		arch.getRPG().read();
		//the externalbus1 must contains the number 11
		assertEquals(11, arch.getExtbus1().get());
		
		//PC must be pointing ONE position after its original value, because this command has no parameters!
		arch.getPC().read();
		assertEquals(51, arch.getExtbus1().get());

	}
	
//	@Test
	public void testMoveRegReg() {
		Architecture arch = new Architecture();

		//storing the number 1 in the memory, in position 31
		arch.getMemory().getDataList()[31]=1;
		//storing the number 0 in the memory, in position 32
		arch.getMemory().getDataList()[32]=0;
		//making PC points to position 30
		arch.getExtbus1().put(30);
		arch.getPC().store();
		
		
		//now setting the registers values
		arch.getExtbus1().put(45);
		arch.getRegistersList().get(0).store(); //RPG0 has 45
		arch.getExtbus1().put(99);
		arch.getRegistersList().get(1).store(); //RPG1 has 99
		
		//executing the command move REG1 REG0.
		arch.moveRegReg();
		
		//testing if both REG1 and REG0 store the same value: 99
		arch.getRegistersList().get(0).read();
		assertEquals(99, arch.getExtbus1().get());
		arch.getRegistersList().get(1).read();
		assertEquals(99, arch.getExtbus1().get());
		
		//Testing if PC points to 3 positions after the original
		//PC was pointing to 30; now it must be pointing to 33
		arch.getPC().read();assertEquals(33, arch.getExtbus1().get());
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
