# Character Visualizer GUI
This JavaFX application displays information about the letter frequencies of a chosen text file. The user can choose which text file to analyze by typing its name in the TextField at the top of the window. The chosen file needs to be in the same directory as the application. If the application can't find the selected file, an error message will appear beneath the TextField:

![errorChart](https://user-images.githubusercontent.com/57165307/120226431-30936580-c215-11eb-8ddb-ce407950295b.png)

When the appropriate file is found, the default settings of the application are to display the letter frequencies in a PieChart, with the slices of the PieChart being ordered by letters that appear most to letters that appear the least, and with the labels of each slice displaying the letter and its frequency. However, by using the lower TextField and RadioButtons, these settings can be adjusted; The lower TextField can be used to select the amount of letters to display in the chart. This is useful when the user wants to see how many times the X most frequenct letters appear. The RadioButtons on the bottom can be used by the user to have the information displayed in a BarChart instead of a PieChart, to display the slices/bars alphabetically instead of by frequency, and the label the slices/bars with the percent that the respective letter appears instead of its frequency. 

![pieChart](https://user-images.githubusercontent.com/57165307/120226445-35f0b000-c215-11eb-9bd8-6f12b2de25b5.png)

![barChart](https://user-images.githubusercontent.com/57165307/120226447-38eba080-c215-11eb-9287-93247255af42.png)

Note: This project was based on a class assignment from CSc 221 - Software Design Lab where we had to recreate the PieChart JavaFX class and use it to display the character frequencies of "Alice in Wonderland" (see the Pie-Chart-GUI repository: https://github.com/danrose499/Pie-Chart-GUI). By using the existing PieChart and BarChart classes from the JavaFX library, the features of that project were extended for this project with the additional features explained above.
