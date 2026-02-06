// ResourcesScreen.kt - FINAL with PURPLE colors and fixed image handling
package com.example.app

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

data class ResourceLink(
    val title: String,
    val url: String,
    val description: String
)

@Composable
fun ResourcesScreen() {
    var selectedTab by remember { mutableStateOf("topic") }
    var topicInput by remember { mutableStateOf("") }
    var currentTopic by remember { mutableStateOf("") }
    var summary by remember { mutableStateOf("") }
    var resourceLinks by remember { mutableStateOf<List<ResourceLink>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showResults by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isLoading = true
            showResults = false
            errorMessage = ""
            summary = ""
            resourceLinks = emptyList()

            scope.launch {
                try {
                    val extractedText = extractTextFromImage(context, it)

                    if (extractedText.isNotBlank() && extractedText.length > 5) {
                        val topic = extractKeywords(extractedText)
                        currentTopic = topic

                        val result = searchTopic(topic)
                        summary = result.first
                        resourceLinks = result.second
                        isLoading = false
                        showResults = true
                    } else {
                        // NO RESULTS if can't read text
                        summary = ""
                        resourceLinks = emptyList()
                        errorMessage = "Could not extract readable text from this image. Please try:\n• A clearer image\n• Better lighting\n• Typed or printed text (not handwritten)"
                        isLoading = false
                        showResults = false
                    }
                } catch (e: Exception) {
                    summary = ""
                    resourceLinks = emptyList()
                    errorMessage = "Failed to analyze image: ${e.localizedMessage}"
                    isLoading = false
                    showResults = false
                }
            }
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        // Header with PURPLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF6366F1), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "R", fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Resources",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )
                Text(
                    text = "AI-powered learning assistant",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Tab Buttons with PURPLE
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedTab = "topic"
                        showResults = false
                        summary = ""
                        resourceLinks = emptyList()
                        errorMessage = ""
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedTab == "topic") Color(0xFF6366F1) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Text(
                    text = "Ask a Topic",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp),
                    color = if (selectedTab == "topic") Color.White else Color(0xFF1F2937),
                    fontWeight = if (selectedTab == "topic") FontWeight.Bold else FontWeight.Normal
                )
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedTab = "image"
                        showResults = false
                        summary = ""
                        resourceLinks = emptyList()
                        errorMessage = ""
                    },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedTab == "image") Color(0xFF6366F1) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Text(
                    text = "Analyze Image",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp),
                    color = if (selectedTab == "image") Color.White else Color(0xFF1F2937),
                    fontWeight = if (selectedTab == "image") FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedTab == "topic") {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Ask About Any Topic",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = topicInput,
                                onValueChange = { topicInput = it },
                                placeholder = { Text("e.g., Photosynthesis, World War II") },
                                modifier = Modifier.weight(1f),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true
                            )

                            IconButton(
                                onClick = {
                                    if (topicInput.isNotEmpty()) {
                                        currentTopic = topicInput
                                        isLoading = true
                                        showResults = false
                                        errorMessage = ""
                                        scope.launch {
                                            try {
                                                val result = searchTopic(topicInput)
                                                summary = result.first
                                                resourceLinks = result.second
                                                isLoading = false
                                                showResults = true
                                            } catch (e: Exception) {
                                                summary = ""
                                                resourceLinks = emptyList()
                                                errorMessage = "Error: ${e.message}"
                                                isLoading = false
                                                showResults = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color(0xFF6366F1), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Search",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Get a quick summary with educational resources",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Analyze an Image",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 2.dp,
                            color = Color(0xFFE5E7EB)
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "+", fontSize = 48.sp, color = Color(0xFF6B7280))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap to upload an image",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                            Text(
                                text = "Diagrams, notes, textbook pages",
                                fontSize = 12.sp,
                                color = Color(0xFF9CA3AF)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6366F1)
                        )
                    ) {
                        Text(
                            text = "Analyze Image",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Upload photos of diagrams, notes, or textbook pages",
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading with PURPLE
        if (isLoading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF6366F1))
                }
            }
        }

        // Error message
        if (errorMessage.isNotEmpty() && !isLoading) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
            ) {
                Text(
                    text = errorMessage,
                    fontSize = 14.sp,
                    color = Color(0xFFEF4444),
                    modifier = Modifier.padding(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Results - ONLY SHOW IF WE HAVE ACTUAL DATA
        if (showResults && !isLoading && summary.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = summary,
                        fontSize = 14.sp,
                        color = Color(0xFF1F2937),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Links - ONLY SHOW IF WE HAVE LINKS
        if (showResults && !isLoading && resourceLinks.isNotEmpty()) {
            Text(
                text = "Educational Resources: $currentTopic",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(12.dp))

            resourceLinks.forEach { resource ->
                ResourceLinkCard(resource = resource)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        // Empty state
        if (!showResults && !isLoading && errorMessage.isEmpty()) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (selectedTab == "topic") "?" else "i",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9CA3AF)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ready to learn something new?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (selectedTab == "topic")
                        "Type any topic to get started"
                    else
                        "Upload an image to analyze",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
fun ResourceLinkCard(resource: ResourceLink) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse(resource.url)
                )
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = resource.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1F2937)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = resource.description,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = resource.url.take(50) + if (resource.url.length > 50) "..." else "",
                    fontSize = 11.sp,
                    color = Color(0xFF6366F1)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = ">",
                fontSize = 20.sp,
                color = Color(0xFF9CA3AF),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Extract text from image using ML Kit
suspend fun extractTextFromImage(context: android.content.Context, uri: Uri): String {
    return withContext(Dispatchers.IO) {
        try {
            val image = InputImage.fromFilePath(context, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            val result = recognizer.process(image).await()
            val text = result.text.trim()

            recognizer.close()

            // Return the extracted text
            text
        } catch (e: Exception) {
            // Return empty string if failed
            ""
        }
    }
}

// Extract keywords from text
fun extractKeywords(text: String): String {
    val commonWords = setOf("the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
        "of", "with", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had",
        "do", "does", "did", "will", "would", "should", "could", "may", "might", "must", "can",
        "this", "that", "these", "those", "i", "you", "he", "she", "it", "we", "they", "what",
        "when", "where", "who", "which", "why", "how", "all", "each", "every", "both", "few",
        "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same",
        "so", "than", "too", "very", "just", "about")

    val words = text.lowercase()
        .replace(Regex("[^a-z\\s]"), " ")
        .split("\\s+".toRegex())
        .filter { it.length > 3 && it !in commonWords }
        .distinct()
        .take(3)

    return if (words.isNotEmpty()) {
        words.joinToString(" ")
    } else {
        text.split("\\s+".toRegex()).take(5).joinToString(" ").trim()
    }
}

// Search topic
suspend fun searchTopic(topic: String): Pair<String, List<ResourceLink>> {
    return withContext(Dispatchers.IO) {
        val summary = getAISummary(topic)
        val links = getEducationalLinks(topic)
        Pair(summary, links)
    }
}

// Get AI summary from Wikipedia
fun getAISummary(topic: String): String {
    return try {
        val encodedTopic = URLEncoder.encode(topic, "UTF-8")
        val apiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/$encodedTopic"

        val url = URL(apiUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = reader.readText()
            reader.close()
            connection.disconnect()

            val json = JSONObject(response)
            val extract = json.optString("extract", "No summary available")

            extract
        } else {
            connection.disconnect()
            "Could not find information about '$topic'. Try being more specific."
        }
    } catch (e: Exception) {
        "Error fetching summary: ${e.message}"
    }
}

// Get educational links for the topic
fun getEducationalLinks(topic: String): List<ResourceLink> {
    val links = mutableListOf<ResourceLink>()
    val encodedTopic = URLEncoder.encode(topic, "UTF-8")

    links.add(
        ResourceLink(
            "Khan Academy: $topic",
            "https://www.khanacademy.org/search?search_again=1&page_search_query=$encodedTopic",
            "Free video lessons and practice"
        )
    )

    links.add(
        ResourceLink(
            "Wikipedia: $topic",
            "https://en.wikipedia.org/wiki/$encodedTopic",
            "Encyclopedia article"
        )
    )

    links.add(
        ResourceLink(
            "YouTube: $topic Tutorial",
            "https://www.youtube.com/results?search_query=$encodedTopic+tutorial",
            "Video tutorials and explanations"
        )
    )

    links.add(
        ResourceLink(
            "Coursera: $topic",
            "https://www.coursera.org/search?query=$encodedTopic",
            "University courses online"
        )
    )

    links.add(
        ResourceLink(
            "MIT OpenCourseWare: $topic",
            "https://ocw.mit.edu/search/?q=$encodedTopic",
            "Free MIT course materials"
        )
    )

    return links
}