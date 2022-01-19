package simulation;

import entities.Entity;
import entities.TreeNode;
import math.Vector2D;
import visuals.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Simulation implements KeyListener, Runnable
{

	public final static int BLOCKSIZE = 50;
	private EntityController entityController;
	private RenderController rc;
	public static boolean running = false;
	private StatsContainer sc = new StatsContainer("0.09", "");
	private int iteration = 0;
	Thread t;


	public static void main(String[] args)
	{
		Entity e = new Entity(new Vector2D(50, 50));
		Entity e2 = new Entity(new Vector2D(50, 50));
		e2.setBrain(e.getBrain().copy());
		e.getBrain().getRoot().printTree();
		System.out.println("---------------");
		e2.getBrain().getRoot().printTree();
		e.getBrain().mutate(0.5);
		System.out.println("---------------");
		e.getBrain().getRoot().printTree();
		System.out.println("---------------");
		e2.getBrain().getRoot().printTree();

		//new Simulation();
	}

	public Simulation()
	{
		init();
	}

	public void init()
	{
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
		logger.info("Booting..");

		String mapPath = "res/test_map.csv";

		rc = new RenderController();

		EnvironmentController envController = new EnvironmentController(BLOCKSIZE);
		envController.loadMapFromCSVFile(mapPath);

		EnvironmentRenderer envRenderer = new EnvironmentRenderer(envController);
		rc.addRenderer(envRenderer);

		entityController = new EntityController(envController);
		EntityRenderer entityRenderer = new EntityRenderer(null, entityController);
		entityController.createInitialEntities(100);

		sc.setValue("map_name", mapPath);
		StatsRenderer sr = new StatsRenderer(sc, (envController.getMap().getSizeX()*BLOCKSIZE) + 20);
		rc.addRenderer(sr);
		rc.addRenderer(entityRenderer);

		Frame frame = envRenderer.createMapMatchingFrame();
		frame.getJFrame().addKeyListener(this);
		frame.setRenderController(rc);
		rc.setFrame(frame);
		rc.triggerRepaint();

		this.running = true;
		t = new Thread(this);
		t.run();

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyChar() =='u')
		{
			entityController.update();
			rc.triggerRepaint();
			this.iteration++;
			sc.setValue("iteration", String.valueOf(this.iteration));
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void run() {
		while (running)
		{
			iteration++;
			entityController.update();
			rc.triggerRepaint();

			sc.setValue("iteration", String.valueOf(this.iteration));
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}
}
