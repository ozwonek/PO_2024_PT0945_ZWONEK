# <center> Darwin Simulation <center> #
# Autors
* Natalia Czajak 
* Aleksandra Zwonek 

# Information

This project was created during object-oriented programming classes in the 3rd semester of the Computer Science program at the Faculty of Computer Science at AGH University of Science and Technology. It was developed in two-person teams. The entire project is implemented in Java, using JavaFX for the graphical interface.

# Project Description

Darwin World is a simulation that represents the process of evolution in a certain rectangular 2D world. In this world, there are animals and plants that go through a specific cycle each day. Additionally, in our version of the Darwin Simulation, we apply two variants:  
* **Crawling Jungle** – a variant related to the plant growth cycle.  
* **Old Age is No Joy** – a variant related to animal behavior.  

Each animal has its own statistics; we can track its age, energy, number of offspring, etc. Animals differ from each other, as each has its own genotype that defines its movement.

## Daily Cycle

* **World Cleanup** – dead animals are removed from the board.  
* **Animal Movement**  
* **Eating** – animals eat, but if multiple animals are on the same tile, the one with the highest energy wins.  
* **Reproduction** – the two strongest and well-fed individuals reproduce.  
* **Growth** – plants grow in "random" locations.  

# Project Features

* Ability to manually configure the world or load configurations from a file.  
* Ability to run multiple simulations simultaneously.  
* Ability to track a selected animal and display its statistics.  
* Graphical interface displaying each day's events.  
* Option to save daily statistics to a CSV file.  

# World Appearance

The world is a 2D board with two parameters: height and width. A single tile can contain at most one plant, but there is no limit to the number of animals.  

Animals cannot cross the top and bottom borders. The world is designed to simulate the Earth; therefore, if an animal tries to cross the side border, it will be transported to the opposite side.  

Plants appear in certain areas with higher probability, such as the Equator.

# Explanation of Variants

### Crawling Jungle
Each day, plants have a higher chance of appearing not only on the Equator but also on neighboring tiles.

### Old Age is No Joy
As animals age, they will occasionally skip their movements.
