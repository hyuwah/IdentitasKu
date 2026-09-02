package com.muhammadwahyudin.identitasku.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muhammadwahyudin.identitasku.R
import com.muhammadwahyudin.identitasku.data.model.DataWithDataType
import com.muhammadwahyudin.identitasku.ui.home.contract.HomeUiEvent
import com.muhammadwahyudin.identitasku.ui.theme.AppTheme
import com.muhammadwahyudin.identitasku.utils.DbUtils

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IdentitasItem(data: DataWithDataType, onAction: (HomeUiEvent.OnItemAction) -> Unit, modifier: Modifier = Modifier, position: Int = -1) {
    Card(
        colors = CardDefaults.outlinedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.outlinedCardElevation(),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                state = rememberTextFieldState(initialText = data.value),
                readOnly = true,
                leadingIcon = {
                    Image(
                        painter = painterResource(data.type().iconRes),
                        contentDescription = null,
                        modifier = Modifier
                            .size(28.dp)
                    )
                },
                colors = TextFieldDefaults.colors().copy(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                    focusedTrailingIconColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(8.dp),
                label = {
                    Text(data.typeName)
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onAction(
                                HomeUiEvent.OnItemAction.CopyValueToClipboard(data.value, data.typeName)
                            )
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_content_copy_white_24dp),
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Column (
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    if (!data.attr2.isNullOrEmpty()) {
                        Text(
                            text = data.attr2.orEmpty(),
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!data.attr1.isNullOrEmpty()) {
                        Text(
                            data.attr1.orEmpty(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                val size = SplitButtonDefaults.ExtraSmallContainerHeight
                var checked by remember { mutableStateOf(false) }
                Box {
                    SplitButtonLayout(
                        leadingButton = {
                            SplitButtonDefaults.OutlinedLeadingButton(
                                onClick = {
                                    onAction(HomeUiEvent.OnItemAction.ShareItem(data))
                                },
                                shapes = SplitButtonDefaults.leadingButtonShapesFor(size),
                                contentPadding = SplitButtonDefaults.leadingButtonContentPaddingFor(size),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(SplitButtonDefaults.leadingButtonIconSizeFor(size)),
                                )
                                Text(
                                    text = "Share",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        },
                        trailingButton = {
                            SplitButtonDefaults.OutlinedTrailingButton(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                },
                                shapes = SplitButtonDefaults.trailingButtonShapesFor(size),
                                contentPadding = SplitButtonDefaults.trailingButtonContentPaddingFor(size),
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    modifier = Modifier.size(SplitButtonDefaults.leadingButtonIconSizeFor(size)),
                                )
                            }
                        },
                    )
                    DropdownMenu(expanded = checked, onDismissRequest = { checked = false }) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onAction(HomeUiEvent.OnItemAction.EditItem(data))
                                checked = false
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onAction(HomeUiEvent.OnItemAction.DeleteItem(position, data))
                                checked = false
                            },
                            leadingIcon = { Icon(Icons.Default.DeleteForever, contentDescription = null) },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun IdentitasItemPreview() {
    AppTheme {
        IdentitasItem(
            data = DbUtils.dataWithDataType,
            onAction = {},
        )
    }
}