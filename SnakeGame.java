package application;

import javafx.animation.KeyFrame;

import javafx.animation.Timeline;

import javafx.application.Application;

import javafx.scene.Scene;

import javafx.scene.input.KeyCode;

import javafx.scene.layout.Pane;

import javafx.scene.paint.Color;

import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;

import javafx.util.Duration;



import java.util.LinkedList;

import java.util.Random;



public class SnakeGame extends Application {

    // Game settings

    private static final int BLOCK_SIZE = 20;

    private static final int WIDTH = 30;  // Game board width in blocks

    private static final int HEIGHT = 20; // Game board height in blocks



    // Snake body and game components

    private LinkedList<Rectangle> snake;

    private Rectangle food;

    private Direction direction = Direction.RIGHT;

    private boolean running = false;

    private boolean moved = false;



    @Override

    public void start(Stage primaryStage) {

        Pane root = new Pane();

        root.setPrefSize(WIDTH * BLOCK_SIZE, HEIGHT * BLOCK_SIZE);



        // Initialize the snake and food

        snake = new LinkedList<>();

        for (int i = 0; i < 3; i++) {

            Rectangle rect = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);

            rect.setFill(Color.GREEN);

            rect.setX(BLOCK_SIZE * (3 - i));

            rect.setY(BLOCK_SIZE * 3);

            snake.add(rect);

        }



        // Add the snake body to the pane

        root.getChildren().addAll(snake);



        // Create food

        food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);

        food.setFill(Color.RED);

        placeFood(root);



        // Game loop

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {

            if (!running) return;

            moveSnake(root);

            checkCollision(root);

            moved = true;

        }));

        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();



        // Set up controls

        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {

            if (!running && (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN ||

                    e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT)) {

                running = true;

            }



            if (moved) {

                switch (e.getCode()) {

                    case UP:

                        if (direction != Direction.DOWN) direction = Direction.UP;

                        break;

                    case DOWN:

                        if (direction != Direction.UP) direction = Direction.DOWN;

                        break;

                    case LEFT:

                        if (direction != Direction.RIGHT) direction = Direction.LEFT;

                        break;

                    case RIGHT:

                        if (direction != Direction.LEFT) direction = Direction.RIGHT;

                        break;

                    default:

                        break;

                }

                moved = false;

            }

        });



        primaryStage.setScene(scene);

        primaryStage.setTitle("Snake Game");

        primaryStage.show();

    }



    // Move the snake in the current direction

    private void moveSnake(Pane root) {

        // Get the current head position

        Rectangle head = snake.getFirst();

        double headX = head.getX();

        double headY = head.getY();



        // Determine new position

        double newX = headX;

        double newY = headY;

        switch (direction) {

            case UP -> newY -= BLOCK_SIZE;

            case DOWN -> newY += BLOCK_SIZE;

            case LEFT -> newX -= BLOCK_SIZE;

            case RIGHT -> newX += BLOCK_SIZE;

        }



        // Create new head

        Rectangle newHead = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);

        newHead.setFill(Color.GREEN);

        newHead.setX(newX);

        newHead.setY(newY);



        // Add new head to the snake

        snake.addFirst(newHead);

        root.getChildren().add(newHead);



        // Remove the tail if not eating food

        if (newX != food.getX() || newY != food.getY()) {

            Rectangle tail = snake.removeLast();

            root.getChildren().remove(tail);

        } else {

            placeFood(root);

        }

    }



    // Check for collisions (self or walls)

    private void checkCollision(Pane root) {

        Rectangle head = snake.getFirst();

        // Check wall collision

        if (head.getX() < 0 || head.getX() >= WIDTH * BLOCK_SIZE ||

                head.getY() < 0 || head.getY() >= HEIGHT * BLOCK_SIZE) {

            running = false;

        }



        // Check self-collision

        for (int i = 1; i < snake.size(); i++) {

            if (head.getX() == snake.get(i).getX() && head.getY() == snake.get(i).getY()) {

                running = false;

            }

        }

    }



    // Place food at a random position

    private void placeFood(Pane root) {

        Random random = new Random();

        int x = random.nextInt(WIDTH) * BLOCK_SIZE;

        int y = random.nextInt(HEIGHT) * BLOCK_SIZE;

        food.setX(x);

        food.setY(y);



        // Ensure food doesn't appear on the snake

        for (Rectangle part : snake) {

            if (part.getX() == x && part.getY() == y) {

                placeFood(root);

                return;

            }

        }

        root.getChildren().add(food);

    }



    // Direction enum

    private enum Direction {

        UP, DOWN, LEFT, RIGHT

    }



    public static void main(String[] args) {

        launch(args);

    }

}

