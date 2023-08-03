package com.example.tipcalculatorcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculatorcompose.ui.theme.TipCalculatorComposeTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TipCalculatorComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TipTimeScreen()
                }
            }
        }
    }

    @Composable
    fun TipTimeScreen(){

        var amountInput by remember { mutableStateOf(" ") }
        var tipInput  by remember { mutableStateOf("") }
        var roundUp by remember {mutableStateOf(false)}

        val amount = amountInput.toDoubleOrNull() ?: 0.0
        val tipPercent = tipInput.toDoubleOrNull() ?: 0.0
        val tip = calculateTip(amount, tipPercent, roundUp)
        val focusManager = LocalFocusManager.current



        Column(modifier = Modifier.padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Text(text = stringResource(R.string.calculate_tip),
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(16.dp))
            EditNumberField(keyboardActions = KeyboardActions(onNext = {focusManager.moveFocus(
                FocusDirection.Down)}),keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next), value = amountInput, onValueChanged = {amountInput = it}, label = R.string.bill_amount, modifier = Modifier)
            EditNumberField(keyboardActions = KeyboardActions(onDone = {focusManager.clearFocus()}),keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),value = tipInput, onValueChanged = {tipInput = it},label = R.string.how_was_the_service, modifier = Modifier)
            RoundTheTipRow(roundUp = roundUp, onRoundUpChanged = {roundUp = it})
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.tip_amount, tip),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

        }
    }

    @Composable
    fun EditNumberField(keyboardActions: KeyboardActions,keyboardOptions: KeyboardOptions, value: String, onValueChanged: (String) -> Unit, @StringRes label: Int, modifier: Modifier) {
        TextField(
            value = value,
            onValueChange = onValueChanged,
            //it aqui é o parametro da função onValueChange, entao
            // amountInput recebe o que foi digitado
            label = {Text(stringResource(label))},
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
    }

    @Composable
    fun RoundTheTipRow(roundUp: Boolean, onRoundUpChanged: (Boolean) -> Unit,modifier: Modifier = Modifier){
        Row(modifier = modifier.fillMaxWidth().size(48.dp), verticalAlignment = Alignment.CenterVertically){
            Text(text = stringResource(R.string.round_up_tip))
            Switch(
                checked = roundUp,
                onCheckedChange = onRoundUpChanged,
                modifier = modifier.fillMaxWidth().wrapContentWidth(Alignment.End),
                colors = SwitchDefaults.colors(uncheckedThumbColor = Color.DarkGray)
            )
        }
    }

    private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean) : String{
        var tip = tipPercent / 100 * amount
        if(roundUp) {
            tip = kotlin.math.ceil(tip)
        }
        return NumberFormat.getCurrencyInstance().format(tip)
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview(){
        TipCalculatorComposeTheme {
            TipTimeScreen()
        }
    }
}

