*** Settings ***
Library    RequestsLibrary
Library    Collections
Library    DateTime
Resource    resource_api_event.robot

*** Keywords ***
# --- SETUP E VALIDAÇÕES GERAIS ---
#teste

Criar Sessao API
    [Documentation]    Cria uma sessão HTTP persistente.
    Create Session    api_session    ${BASE_URL}    verify=True

Executar POST e Validar Status
    [Documentation]    Executa POST com payload e valida status code.
    [Arguments]    ${payload}    ${expected_status}
    
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${response}=    POST On Session    api_session    ${ENDPOINT}
    ...    json=${payload} 
    ...    headers=${headers}
    ...    expected_status=any

    Should Be Equal As Integers    ${response.status_code}    ${expected_status}
    RETURN    ${response}

Validar Response Sucesso 201
    [Arguments]    ${response}    ${expected_name}
    [Documentation]    Valida o conteúdo da resposta 201.
    ${response_json}=    Set Variable    ${response.json()}
    Should Not Be Empty    ${response_json['id']}
    Should Not Be Empty    ${response_json['createdAt']}
    Should Not Be Empty    ${response_json['updatedAt']}
    
    # Validações de dados de sucesso
    Should Be Equal    ${response_json['name']}    ${expected_name}
    Should Be Equal As Integers    ${response_json['capacity']}    ${SUCCESS_CAPACITY}

# --- GERAÇÃO DE PAYLOADS COM FAKER ---

Gerar Nome Evento Aleatorio
    [Documentation]    Gera um nome de evento único e aleatório
    ${random_name}=    FakerLibrary.Word
    ${timestamp}=    Get Current Date    result_format=%H%M%S
    ${event_name}=    Set Variable    Evento ${random_name} ${timestamp}
    RETURN    ${event_name}

Gerar Payload Sucesso Completo Aleatório
    [Documentation]    Gera um payload com todos os campos válidos.
    ${event_name}=    Gerar Nome Evento Aleatorio
    &{payload}=    Create Dictionary
    ...    name=${event_name}
    ...    description=Descrição completa do evento.
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    ageRestriction=18
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_FINAL}
    RETURN    ${payload}

Gerar Payload Sucesso Completo 
    [Documentation]    Gera um payload com todos os campos válidos.
    &{payload}=    Create Dictionary
    ...    name=teste
    ...    description=Descrição completa do evento.
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    ageRestriction=18
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_FINAL}
    RETURN    ${payload}

Gerar Payload Sucesso Minimo
    [Documentation]    Gera um payload apenas com campos obrigatórios válidos.
    ${event_name}=    Gerar Nome Evento Aleatorio
    &{payload}=    Create Dictionary
    ...    name=${event_name}
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_FINAL}
    RETURN    ${payload}

Gerar Payload Datas Iguais
    [Documentation]    Gera um payload onde data inicial é igual à final.
    ${event_name}=    Gerar Nome Evento Aleatorio
    &{payload}=    Create Dictionary
    ...    name=${event_name}
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_INITIAL}
    RETURN    ${payload}

# --- GERAÇÃO DE PAYLOADS DE FALHA ---

Gerar Payload Campo Ausente
    [Arguments]    ${missing_field}
    [Documentation]    Remove um campo obrigatório do payload de sucesso mínimo.
    ${event_name}=    Gerar Nome Evento Aleatorio
    &{payload}=    Create Dictionary
    ...    name=${event_name}
    ...    location=${SUCCESS_LOCATION}
    ...    capacity=${SUCCESS_CAPACITY}
    ...    dateInitial=${FUTURE_DATE_INITIAL}
    ...    dateFinal=${FUTURE_DATE_FINAL}
    Remove From Dictionary    ${payload}    ${missing_field}
    RETURN    ${payload}

Gerar Payload Campo Negativo
    [Arguments]    ${field_name}    ${field_value}
    [Documentation]    Define um valor inválido (negativo) para um campo numérico.
    &{payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    ${field_name}=${field_value}
    RETURN    ${payload}

Gerar Payload Data Passada
    [Documentation]    Define dateInitial como uma data passada.
    ${past_date}=    Convert Date    2020-01-01T10:00:00    result_format=%Y-%m-%dT%H:%M:%S
    &{payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    dateInitial=${past_date}
    RETURN    ${payload}

Gerar Payload Data Final Anterior
    [Documentation]    Define dateFinal anterior a dateInitial.
    &{payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    dateInitial=2030-10-10T12:00:00
    Set To Dictionary    ${payload}    dateFinal=2030-10-10T11:00:00
    RETURN    ${payload}

Gerar Payload Formato Data Invalido
    [Documentation]    Define um formato de data inválido.
    &{payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    dateInitial=10/10/2030 18:00
    RETURN   ${payload}

Gerar Payload Tipo Invalido
    [Arguments]    ${field_name}    ${invalid_value}
    [Documentation]    Define um tipo de dado inválido (ex: int em vez de string).
    &{payload}=    Gerar Payload Sucesso Minimo
    Set To Dictionary    ${payload}    ${field_name}=${invalid_value}
    RETURN    ${payload}