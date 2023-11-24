package br.senai.sp.jandira.formativa

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.formativa.retrofit.ApiService
import br.senai.sp.jandira.formativa.retrofit.RetrofitHelper
import br.senai.sp.jandira.formativa.ui.theme.FormativaTheme
import coil.compose.AsyncImage
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FormativaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting() {


    val context = LocalContext.current

    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        var login by remember { mutableStateOf(TextFieldValue("")) }
        TextField(
            value = login,
            singleLine = true,
            onValueChange = { x -> login = x },
            label = { Text(text = "login") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        var senha by remember { mutableStateOf(TextFieldValue("")) }
        var passwordVisibility: Boolean by remember { mutableStateOf(false) }
        TextField(
            value = senha,
            singleLine = true,
            onValueChange = { x -> senha = x },
            label = { Text(text = "senha") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Text(text = "ver")
                }
            }
        )

        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .padding(4.dp)
                .height(200.dp)
                .width(200.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop,
        )


        Button(onClick = {
            launcher.launch("image/*")
            Log.d("teset","imagem uri: $imageUri")

        }) {
            Text(text = "Selecionar imagem")
        }

        Button(onClick = {
            Log.d("teset222","imagem uri: $imageUri , login: ${login.text}, senha ${senha.text}")
            if (login.text != "" && senha.text != "") {
                 criarUsuario(login.text,senha.text)
                }


            },
            ) {
            Text(text = "Fazer Cadastro", fontSize = 30.sp)

        }
    }
}

fun criarUsuario(login: String, senha: String) {
    CoroutineScope(Dispatchers.Default).launch {
        var apiService: ApiService = RetrofitHelper.getInstance().create(ApiService::class.java)

        //MONTAGEM DO CORPO DE DADOS EM JSON
        val body = JsonObject().apply {
            addProperty("login", login)
            addProperty("senha", senha)
        }

        //ENVIO DA REQUISIÇÃO DE CADASTRO DE CATEGORIA
        val result = apiService.createUser(body)

        //VERIFICANDO A RESPOSTA DA REQUISIÇÃO
        if(result.isSuccessful){
            val msg = result.body()?.get("mensagemStatus")
            Log.e("CREATE-USER", "STATUS: ${msg}")
        }else{
            Log.e("CREATE-USER", "ERROR: ${result.message()}")
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    FormativaTheme {
        Greeting()
    }
}