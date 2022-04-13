using System;
using System.Collections.Generic;
using System.Linq;
using Excel = Microsoft.Office.Interop.Excel;
using System.Windows;
using Microsoft.Win32;

namespace Hw9_10
{
    public partial class MainWindow : Window
    {
        String filename = "";
        public MainWindow()
        {
            InitializeComponent();
        }

        //Open file search and choose excel file for processing
        private void menuOpen_Click(object sender, RoutedEventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Title = "Select an Excel File to Process";
            openFileDialog.Filter = "Excel files (*.xlsx) | *.xlsx| All Files(*.*)|*.*";

            // set file and inform user of next step
            if (openFileDialog.ShowDialog() == true)
            {
                filename = openFileDialog.FileName;
                lbl_Output.Content = "Select data to continue";
            }
        }

        //give user option to exit application
        private void menuExit_Click(object sender, RoutedEventArgs e)
        {
            MessageBoxResult answer;
            answer = MessageBox.Show("Really Exit?", "Close Application", MessageBoxButton.YesNo, MessageBoxImage.Question);
            if (answer == MessageBoxResult.Yes)
                Application.Current.Shutdown();
        }

        private void menuRun_Report_Click(object sender, RoutedEventArgs e)
        {
            // make sure file exist before processing
            if (!System.IO.File.Exists(filename))
            {
                lbl_Output.Content = "No data to process";
                return;
            }

            // setup excel 
            Excel.Application myApp = new Excel.Application();
            Excel.Workbook myBook;
            myApp.Visible = false;
            myBook = myApp.Workbooks.Open(filename);

            // determine which choices are marked
            if ((bool)_Item.IsChecked)
                getItemValues(myBook);
            else if ((bool)_Region.IsChecked)
                getRegionValues(myBook);
            else if ((bool)_Sales_Rep.IsChecked)
                getSalesReps(myBook);

            myBook.Close();

        }
        // process values of items
        private void getItemValues(Excel.Workbook myBook)
        {
            Excel.Worksheet mySheet;
            Excel.Range myRange;

            mySheet = myBook.Sheets[1];
            myRange = mySheet.UsedRange;

            Dictionary<string, int> units_sold = new Dictionary<string, int>();         // values from excel
            Dictionary<string, double> units_rev = new Dictionary<string, double>();         // values from excel

            string next_item;    // item on the next row, such as pencil or desk
            int next_units;      // how many items were sold
            double next_rev;

            string high;
            string low;

            // loop through all the rows to build the dictionary
            for (int r = 2; r <= myRange.Rows.Count; r++)
            {

                // retrieve next row's data from the Excel file
                next_item = (string)(mySheet.Cells[r, 4] as Excel.Range).Value;
                next_units = (int)(mySheet.Cells[r, 5] as Excel.Range).Value;
                next_rev = (double)(mySheet.Cells[r, 7] as Excel.Range).Value;

                // is that a new item for the dictionary?
                if (!units_sold.ContainsKey(next_item))
                {
                    units_sold.Add(next_item, next_units);
                    units_rev.Add(next_item, next_rev);
                }

                // if item already in list, then add the new values
                else
                {
                    units_sold[next_item] += next_units;
                    units_rev[next_item] += next_rev;
                }
            }

            // set high and low to first item in report
            high = units_sold.ElementAt(0).Key;
            low = units_sold.ElementAt(0).Key;

            //search through values to find highest and lowest items
            foreach (KeyValuePair<string, int> item in units_sold)
            {
                if (item.Value > units_sold[high])
                {
                    high = item.Key;
                }
                else if (item.Value < units_sold[low])
                {
                    low = item.Key;
                }
            }

            if ((bool)_Units_Sold.IsChecked)
            {
                // output least popular item
                if ((bool)_LowestValue.IsChecked)
                {
                    lbl_Output.Content = "Least selling item = " + low + " (" + units_sold[low] + " units)";
                }
                //output most popular item 
                else if ((bool)_HighestValue.IsChecked)
                {
                    lbl_Output.Content = "Most popular item = " + high + " (" + units_sold[high] + " units)";
                }
                //output all item amounts
                else if ((bool)_AllValues.IsChecked)
                {
                    string allValues = "Units sold by item: \n------------------------\n";
                    foreach (KeyValuePair<string, int> item in units_sold)
                    {
                        allValues += item.Key + " - " + item.Value.ToString() + "\n";
                    }
                    lbl_Output.Content = allValues;
                }
            }
            // output revenue 
            else if ((bool)_Revenue.IsChecked)
            {
                // item with least revenue
                if ((bool)_LowestValue.IsChecked)
                {
                    lbl_Output.Content = "Least popular item = " + low + " ($ " + units_rev[low] + " )";
                }
                // item with most revenue
                else if ((bool)_HighestValue.IsChecked)
                {
                    lbl_Output.Content = "Most popular item = " + high + " ($ " + units_rev[high] + " )";
                }
                // all revenue from each item
                else if ((bool)_AllValues.IsChecked)
                {
                    string allValues = "Revenue From All Items: \n------------------------\n";
                    foreach (KeyValuePair<string, double> item in units_rev)
                    {
                        allValues += item.Key + " - $ " + item.Value.ToString() + "\n";
                    }
                    lbl_Output.Content = allValues;
                }
            }
        }

        //process values for regions
        private void getRegionValues(Excel.Workbook myBook)
        {
            Excel.Worksheet mySheet;
            Excel.Range myRange;

            mySheet = myBook.Sheets[1];
            myRange = mySheet.UsedRange;

            Dictionary<string, int> region_units = new Dictionary<string, int>();         // values from excel
            Dictionary<string, double> region_rev = new Dictionary<string, double>();         // values from excel

            string next_region;    // region on the next row 
            int next_units;      // how many items were sold
            double next_rev;

            string high;
            string low;

            // loop through all the rows to build the dictionary
            for (int r = 2; r <= myRange.Rows.Count; r++)
            {

                // retrieve next row's data from the Excel file
                next_region = (string)(mySheet.Cells[r, 2] as Excel.Range).Value;
                next_units = (int)(mySheet.Cells[r, 5] as Excel.Range).Value;
                next_rev = (double)(mySheet.Cells[r, 7] as Excel.Range).Value;

                // is that a new item for the dictionary?
                if (!region_units.ContainsKey(next_region))
                {
                    region_units.Add(next_region, next_units);
                    region_rev.Add(next_region, next_rev);
                }

                // if item already in list, then add the new values
                else
                {
                    region_units[next_region] += next_units;
                    region_rev[next_region] += next_rev;
                }
            }

            // set high and low to first item in report
            high = region_units.ElementAt(0).Key;
            low = region_units.ElementAt(0).Key;

            //search through values to find highest and lowest items
            foreach (KeyValuePair<string, int> item in region_units)
            {
                if (item.Value > region_units[high])
                {
                    high = item.Key;
                }
                else if (item.Value < region_units[low])
                {
                    low = item.Key;
                }
            }

            // determine what is checked and print accordingly 
            if ((bool)_Units_Sold.IsChecked)
            {
                // output least popular item
                if ((bool)_LowestValue.IsChecked)
                {
                    lbl_Output.Content = "Least selling region = " + low + " (" + region_units[low] + " units)";
                }
                //output most popular item 
                else if ((bool)_HighestValue.IsChecked)
                {
                    lbl_Output.Content = "Highest selling region = " + high + " (" + region_units[high] + " units)";
                }
                //output all item amounts
                else if ((bool)_AllValues.IsChecked)
                {
                    string allValues = "Units sold by region: \n------------------------\n";
                    foreach (KeyValuePair<string, int> item in region_units)
                    {
                        allValues += item.Key + " - " + item.Value.ToString() + "\n";
                    }
                    lbl_Output.Content = allValues;
                }
            }
            // output rev
            else if ((bool)_Revenue.IsChecked)
            {
                // smallest revenue
                if ((bool)_LowestValue.IsChecked)
                {
                    lbl_Output.Content = "Region With Least Revenue = " + low + " ($ " + region_rev[low] + " )";
                }
                // highest revenue
                else if ((bool)_HighestValue.IsChecked)
                {
                    lbl_Output.Content = "Region With Most Revenue = " + high + " ($ " + region_rev[high] + " )";
                }
                // all revenue
                else if ((bool)_AllValues.IsChecked)
                {
                    string allValues = "All Region Revenue: \n------------------------\n";
                    foreach (KeyValuePair<string, double> item in region_rev)
                    {
                        allValues += item.Key + " - $ " + item.Value.ToString() + "\n";
                    }
                    lbl_Output.Content = allValues;
                }
            }
        }

        // process values for sales reps
        private void getSalesReps(Excel.Workbook myBook)
        {
            Excel.Worksheet mySheet;
            Excel.Range myRange;

            mySheet = myBook.Sheets[1];
            myRange = mySheet.UsedRange;

            Dictionary<string, int> rep_units = new Dictionary<string, int>();         // values from excel
            Dictionary<string, double> rep_rev = new Dictionary<string, double>();         // values from excel

            string next_rep;    // rep on the next row 
            int next_units;      // how many items were sold
            double next_rev;

            string high;
            string low;

            // loop through all the rows to build the dictionary
            for (int r = 2; r <= myRange.Rows.Count; r++)
            {

                // retrieve next row's data from the Excel file
                next_rep = (string)(mySheet.Cells[r, 3] as Excel.Range).Value;
                next_units = (int)(mySheet.Cells[r, 5] as Excel.Range).Value;
                next_rev = (double)(mySheet.Cells[r, 7] as Excel.Range).Value;

                // is that a new item for the dictionary?
                if (!rep_units.ContainsKey(next_rep))
                {
                    rep_units.Add(next_rep, next_units);
                    rep_rev.Add(next_rep, next_rev);
                }

                // if item already in list, then add the new values
                else
                {
                    rep_units[next_rep] += next_units;
                    rep_rev[next_rep] += next_rev;
                }
            }

            // set high and low to first rep in report
            high = rep_units.ElementAt(0).Key;
            low = rep_units.ElementAt(0).Key;

            //search through values to find highest and lowest reps
            foreach (KeyValuePair<string, int> item in rep_units)
            {
                if (item.Value > rep_units[high])
                {
                    high = item.Key;
                }
                else if (item.Value < rep_units[low])
                {
                    low = item.Key;
                }
            }

            if ((bool)_Units_Sold.IsChecked)
            {
                // output least selling rep
                if ((bool)_LowestValue.IsChecked)
                {
                    lbl_Output.Content = "Least selling region = " + low + " (" + rep_units[low] + " units)";
                }
                //output most selling rep 
                else if ((bool)_HighestValue.IsChecked)
                {
                    lbl_Output.Content = "Highest selling region = " + high + " (" + rep_units[high] + " units)";
                }
                //output all rep sales
                else if ((bool)_AllValues.IsChecked)
                {
                    string allValues = "Units sold by region: \n------------------------\n";
                    foreach (KeyValuePair<string, int> item in rep_units)
                    {
                        allValues += item.Key + " - " + item.Value.ToString() + "\n";
                    }
                    lbl_Output.Content = allValues;
                }
            }
            // revenue from reps
            else if ((bool)_Revenue.IsChecked)
            {
                // rep with the least revenue
                if ((bool)_LowestValue.IsChecked)
                {
                    lbl_Output.Content = "Least Selling Sales Rep = " + low + " ($ " + rep_rev[low] + " )";
                }
                // rep with the most revenue
                else if ((bool)_HighestValue.IsChecked)
                {
                    lbl_Output.Content = "Highest Selling Sales Rep = " + high + " ($ " + rep_rev[high] + " )";
                }
                else if ((bool)_AllValues.IsChecked)
                {
                    string allValues = "All Sales Rep Revenue: \n------------------------\n";
                    foreach (KeyValuePair<string, double> item in rep_rev)
                    {
                        allValues += item.Key + " - $ " + item.Value.ToString() + "\n";
                    }
                    lbl_Output.Content = allValues;
                }
            }
        }
    }
}
