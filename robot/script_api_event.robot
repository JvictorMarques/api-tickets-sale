*** Keywords ***
# --- SETUP E VALIDAÇÕES GERAIS ---

Criar Sessao API
    [Documentation]    Cria uma sessão HTTP persistente.
    Create Session    api_session    ${BASE_URL}    verify=True

Executar POST e Validar Status
    [Arguments]    ${payload}    ${expected_status}
    [Documentation]    Executa a requisição POST com o payload e valida o status code.
    Criar Sessao API
    ${response}=    POST On Session    api_session    ${ENDPOINT}
    ...    json=${payload}
    ...    headers=${HEADERS}

    Should Be Equal As Integers    ${response.status_code}    ${expected_status}
    [Return]    ${response}

Validar Response Sucesso (201)
    [Arguments]    ${response}
    [Documentation]    Valida o conteúdo da resposta 201.
    ${response_json}=    Set Variable    ${response.json()}
    Should Not Be Empty    ${response_json['id']}
    Should Be True    ${response_json['createdAt']} is not None
    Should Be True    ${response_json['updatedAt']} is not None
    
    # Validações de dados de sucesso
    Should Be Equal    ${response_json['name']}    ${SUCCESS_NAME}
    Should Be Equal As Integers    ${response_json['capacity']}    ${SUCCESS_CAPACITY}

Validar Mensagem de Erro
    [Arguments]    ${response}    ${partial_message}
    [Documentation]    Verifica se a resposta de erro contém a mensagem esperada.
    ${response_text}=    Set Variable    ${response.text}
    Should Contain    ${response_text}    ${partial_message}


# --- GERAÇÃO DE PAYLOADS DE SUCESSO ---

Gerar Payload Sucesso Completo
    [Documentation]    Gera um payload com todos os campos válidos.
    &{payload}=    Create Dictionary
    ...    name=${SUCCESS_NAME}
    ...    description=Descrição completa do evento.
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    ageRestriction=18
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_FINAL}
    [Return]    ${payload}

Gerar Payload Sucesso Minimo
    [Documentation]    Gera um payload apenas com campos obrigatórios válidos.
    &{payload}=    Create Dictionary
    ...    name=${SUCCESS_NAME}
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_FINAL}
    [Return]    ${payload}

Gerar Payload Datas Iguais
    [Documentation]    Gera um payload onde data inicial é igual à final.
    &{payload}=    Create Dictionary
    ...    name=${SUCCESS_NAME}
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_INITIAL}
    [Return]    ${payload}


# --- GERAÇÃO DE PAYLOADS DE FALHA ---

Gerar Payload Campo Ausente
    [Arguments]    ${missing_field}
    [Documentation]    Remove um campo obrigatório do payload de sucesso mínimo.
    ${payload}=    Gerar Payload Sucesso Minimo
    Remove From Dictionary    ${payload}    ${missing_field}
    [Return]    ${payload}

Gerar Payload Campo Negativo
    [Arguments]    ${field_name}    ${field_value}
    [Documentation]    Define um valor inválido (negativo) para um campo numérico.
    ${payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    ${field_name}=${field_value}
    [Return]    ${payload}

Gerar Payload Data Passada
    [Documentation]    Define dateInitial como uma data passada.
    ${past_date}=    Convert Date    2020-01-01T10:00:00    result_format=%Y-%m-%dT%H:%M:%S
    ${payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    dateInitial=${past_date}
    [Return]    ${payload}

Gerar Payload Data Final Anterior
    [Documentation]    Define dateFinal anterior a dateInitial.
    ${payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    dateInitial=2030-10-10T12:00:00
    Set To Dictionary    ${payload}    dateFinal=2030-10-10T11:00:00
    [Return]    ${payload}

Gerar Payload Formato Data Invalido
    [Documentation]    Define um formato de data inválido.
    ${payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    dateInitial=10/10/2030 18:00
    [Return]    ${payload}

Gerar Payload Tipo Invalido
    [Arguments]    ${field_name}    ${invalid_value}
    [Documentation]    Define um tipo de dado inválido (ex: int em vez de string).
    ${payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    ${field_name}=${invalid_value}
    [Return]    ${payload}